package boot.conf;

import boot.conf.jdbc.MybatisKit;
import boot.conf.props.DataBase;

import java.util.Set;

/**
 * @author valor.
 */
public class PropInvoke {

    private PropInvoke() { }

    /**
     * 当加载完毕某个节点的配置文件时 将对应的模块进行初始化
     */
    public static void call(Set<Class<?>> set) {
        if (set.contains(DataBase.class)) {
            MybatisKit.build();
        }
    }
}
