package common.utils.line;

/**
 * @author valor.
 */
public interface OnLine<E> {

    /**
     * 处理每一行的文本
     *
     * @param index 行号
     * @param line  文本
     * @param e     返回的参数
     */
    void handle(int index, byte[] line, E e) throws Exception;

}
