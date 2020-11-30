package common.utils.jackson;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author valor.
 */
public class XmlMarker extends BaseMarker<XmlMapper> {

    /* --------------- instance --------------- */

    private XmlMarker() { }

    private static final Object LOCK = new Object();

    private static volatile XmlMarker marker;

    public static XmlMarker instance() {
        if (null == marker) {
            synchronized (LOCK) {
                if (null == marker) {
                    marker = new XmlMarker();
                }
            }
        }

        return marker;
    }

    /* --------------- variable --------------- */

    private static final XmlMapper mapper;

    @Override
    public XmlMapper mapper() {
        return mapper;
    }

    static {
        // 基于woodstox 处理xml
        WstxInputFactory inputFactory = new WstxInputFactory();
        WstxOutputFactory outputFactory = new WstxOutputFactory();
        mapper = new XmlMapper(inputFactory, outputFactory);

        // 在序列化时忽略值为 null 的属性
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 将日期转为时间戳
//        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

}
