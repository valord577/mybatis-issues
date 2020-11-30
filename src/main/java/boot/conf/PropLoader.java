package boot.conf;

import common.utils.StrKit;
import common.utils.enums.CharSet;
import common.utils.line.AtLines;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author valor.
 */
public class PropLoader {

    private static final Logger log = LoggerFactory.getLogger(PropLoader.class);

    private static volatile boolean built = false;

    private static final String _package = "boot.conf.props";

    private static volatile String titleCached;

    // title -> object at {boot.conf.props.*}
    private static volatile Map<Class<?>, Object> propsCached;

    private PropLoader() { }

    public static void build(InputStream in) throws Exception {
        if (!built) {
            synchronized (PropLoader.class) {
                if (!built) {
                    // title -> props at {boot.conf.props.*}
                    Map<String, Map<String, String>> mapProps = new HashMap<>();
                    AtLines.of(in, mapProps, PropLoader::onLine);

                    if (mapProps.isEmpty()) {
                        log.warn("Empty profile(s). Load default props.");
                    } else {

                        propsCached = new HashMap<>();

                        for (Map.Entry<String, Map<String, String>> entry : mapProps.entrySet()) {
                            String title = entry.getKey();
                            Map<String, String> props = entry.getValue();

                            if (props.isEmpty()) {
                                log.warn("Empty profile(s) node: '{}'", title);
                            } else {
                                Class<?> clazz = Class.forName(_package + "." + title);
                                Object propsEntity = clazz.getDeclaredConstructor().newInstance();

                                Field[] fields = clazz.getDeclaredFields();
                                for (Field field : fields) {
                                    String value = props.get(field.getName());
                                    if (StrKit.isBlank(value)) {
                                        continue;
                                    }

                                    field.setAccessible(true);
                                    Class<?> type = field.getType();

                                    if (type == String.class) {
                                        field.set(propsEntity, value);
                                    } else if (type == int.class || type == Integer.class) {
                                        field.setInt(propsEntity, Integer.parseInt(value));
                                    } else if (type == boolean.class || type == Boolean.class) {
                                        field.setBoolean(propsEntity, Boolean.parseBoolean(value));
                                    }
                                }

                                propsCached.put(clazz, propsEntity);
                            }
                        }
                    }

                    built = true;

                    // 初始化某些模块
                    PropInvoke.call(propsCached.keySet());
                }
            }
        }
    }

    private static void isBuilt() {
        if (!built) {
            throw new UnsupportedOperationException("Please invoke 'PropLoader#build' first.");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProps(Class<T> clazz) {
        isBuilt();

        Object props = propsCached.get(clazz);
        if (null == props) {
            throw new NullPointerException("No such entity be defined.");
        }
        return (T) props;
    }

    /**
     * 对每一行配置进行解析 保存至map
     */
    private static void onLine(int index, byte[] line, Map<String, Map<String, String>> map) throws ParseException {
        // 忽略 ' ' or '\t'
        int length = line.length;
        if (length < 3) {
            throw new ParseException("Error Parse Line: '" + new String(line, CharSet.UTF_8.getCharset()) + "'", 1);
        }

        // commented by ';' or '#' at first byte
        byte first = line[0];
        if (first == '#') {
            return;
        }
        if (first == ';') {
            if (length == 3) {
                throw new ParseException("Error Parse Line: '" + new String(line, CharSet.UTF_8.getCharset()) + "'", 1);
            }

            int titleLen = length - 3;
            byte[] title = new byte[titleLen];
            System.arraycopy(line, 2, title, 0, titleLen);
            titleCached = new String(title, CharSet.UTF_8.getCharset());

            map.computeIfAbsent(titleCached, k -> new HashMap<>());
            return;
        }

        final int defaultIndex = -1;

        int ks = defaultIndex;
        int ke = defaultIndex;
        int vs = defaultIndex;
        int ve = defaultIndex;

        boolean isLeft = true;

        for (int i = 0; i < length; i++) {
            byte current = line[i];
            if (current == '=') {
                if (i == 0 || i == length - 1) {
                    // 等号在首位/末位
                    throw new ParseException("Error Parse Line: '" + new String(line, CharSet.UTF_8.getCharset()) + "'", i + 1);
                }

                // 只承认第一个等号有效
                isLeft = false;
                // 复位
                continue;
            }

            if (isLeft) {
                // 等号左边
                if (current != ' ' && current != '\t') {
                    if (i == 0) {
                        ks = i;
                    } else {
                        byte pre = line[i - 1];
                        if ((pre == ' ' || pre == '\t')) {
                            if (ks == defaultIndex) {
                                ks = i;
                            } else {
                                throw new ParseException("Error Parse Line: '" + new String(line, CharSet.UTF_8.getCharset()) + "'", i + 1);
                            }
                        }
                    }

                    byte next = line[i + 1];
                    if (next == '=' || (next == ' ' || next == '\t')) {
                        if (ke == defaultIndex) {
                            ke = i;
                        } else {
                            throw new ParseException("Error Parse Line: '" + new String(line, CharSet.UTF_8.getCharset()) + "'", i + 1);
                        }
                    }
                }
            } else {
                // 等号右边
                if (current != ' ' && current != '\t') {
                    byte pre = line[i - 1];
                    if (pre == '=' || (pre == ' ' || pre == '\t')) {
                        if (vs == defaultIndex) {
                            vs = i;
                        } else {
                            throw new ParseException("Error Parse Line: '" + new String(line, CharSet.UTF_8.getCharset()) + "'", i + 1);
                        }
                    }

                    if (i == length - 1) {
                        if (ve == defaultIndex) {
                            ve = i;
                        }
                    } else {
                        byte next = line[i + 1];
                        if (next == ' ' || next == '\t') {
                            if (ve == defaultIndex) {
                                ve = i;
                            } else {
                                throw new ParseException("Error Parse Line: '" + new String(line, CharSet.UTF_8.getCharset()) + "'", i + 1);
                            }
                        }
                    }
                }
            }
        }

        // 跳过 "  =  " 之类的情况
        if (ks == defaultIndex || ke == defaultIndex || vs == defaultIndex || ve == defaultIndex) {
            throw new ParseException("Error Parse Line: '" + new String(line, CharSet.UTF_8.getCharset()) + "'", 1);
        }

        int keyLen = ke - ks + 1;
        byte[] key = new byte[keyLen];
        System.arraycopy(line, ks, key, 0, keyLen);
        String keyStr = new String(key, CharSet.UTF_8.getCharset());

        int valueLen = ve - vs + 1;
        byte[] value = new byte[valueLen];
        System.arraycopy(line, vs, value, 0, valueLen);
        String valueStr = new String(value, CharSet.UTF_8.getCharset());

        Map<String, String> props = map.get(titleCached);
        if (null == props) {
            throw new ParseException("Error Parse Line: '" + new String(line, CharSet.UTF_8.getCharset()) + "'", 1);
        }
        props.put(keyStr, valueStr);
    }

}
