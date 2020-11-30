package common.utils.line;

import common.utils.Const;
import io.vertx.core.buffer.Buffer;

import java.io.InputStream;

/**
 * @author valor.
 */
public class AtLines {

    private AtLines() { }

    /**
     * 处理文本 {@link Buffer}
     *
     * @param in   buffer
     * @param e    处理后的参数
     * @param task 处理每一行的逻辑
     */
    public static <E> void of(Buffer in, E e, OnLine<E> task) throws Exception {

        // 当前行数 从0开始
        int index = 0;
        // 每次读取buffer大小
        int step = Const.buf_size;
        byte[] buffer = new byte[step];
        // 读取字节数
        int readSize;
        // 总字节大小
        int _total_len = in.length();
        // 暂存不足一行的内容
        byte[] noneLine = Const.EMPTY_BYTE_ARRAY;
        // 暂存内容的长度
        int noneLineLen;

        // 从0开始读取
        int begin = 0;
        int end;
        // 是否最后一次读取
        boolean eof = false;
        while (!eof) {

            end = begin + step;
            if (end > _total_len) {
                eof = true;
                // 防止超出边界
                end = _total_len;
            } else if (end == _total_len) {
                eof = true;
            }
            readSize = end - begin;

            in.getBytes(begin, end, buffer);
            begin = end;

            /* 读取一行 处理一行 */
            // 记录数组 标志
            int start = 0;
            int length;
            // 一行的内容
            byte[] oneLine;

            for (int i = 0; i < readSize; i++) {
                byte current = buffer[i];
                // 处理换行符
                if (current == (byte) '\r' || current == (byte) '\n') {
                    // 当前byte不计算在内
                    length = i - start;

                    if (start == 0) {
                        // 忽略空行
                        noneLineLen = noneLine.length;
                        if (length < 1 && noneLineLen < 1) {
                            start++;
                            continue;
                        }

                        // 兼容上一次buffer最后没有换行符的情况
                        int newLength = length;

                        if (noneLineLen > 0) {
                            newLength += noneLineLen;
                        }

                        oneLine = new byte[newLength];
                        if (noneLineLen > 0) {
                            System.arraycopy(noneLine, 0, oneLine, 0, noneLineLen);
                            System.arraycopy(buffer, start, oneLine, noneLineLen, length);

                            // 初始化noneLine
                            noneLine = Const.EMPTY_BYTE_ARRAY;
                        } else {
                            System.arraycopy(buffer, start, oneLine, 0, newLength);
                        }
                    } else {
                        // 忽略空行
                        if (length < 1) {
                            start++;
                            continue;
                        }

                        oneLine = new byte[length];
                        System.arraycopy(buffer, start, oneLine, 0, length);
                    }

                    // 处理一行的内容
                    task.handle(index, oneLine, e);
                    index++;

                    // 重新定位起始位置
                    start = i + 1;
                    // 如果当前是 '\r' 下一个是'\n' 则直接忽略
                    if (current == (byte) '\r' && (i + 1) < readSize) {
                        byte next = buffer[i + 1];
                        if (next == (byte) '\n') {
                            i++;
                            start++;
                        }
                    }
                } else {
                    if (i == readSize - 1) {
                        // 如果是最后一个字符 则直接存在noneLine中 与下一次buffer组成一行
                        // 当前byte也需要输出 则需要+1
                        length = i - start + 1;
                        // 忽略空行
                        if (length < 1) {
                            start++;
                            continue;
                        }

                        // 如果连续两次都没有换行符 则把两次内容都整合在一起
                        noneLineLen = noneLine.length;
                        if (noneLineLen > 0) {
                            // 保存上一次内容
                            byte[] tmp = new byte[noneLineLen];
                            System.arraycopy(noneLine, 0, tmp, 0, noneLineLen);

                            noneLine = new byte[length + noneLineLen];
                            // 先copy上一次内容
                            System.arraycopy(tmp, 0, noneLine, 0, noneLineLen);
                            // 再保存这一次内容
                            System.arraycopy(buffer, start, noneLine, noneLineLen, length);
                        } else {
                            noneLine = new byte[length];
                            System.arraycopy(buffer, start, noneLine, 0, length);
                        }

                        // 如果是最后一次读取 则直接处理
                        if (eof) {
                            task.handle(index, noneLine, e);
                            index++;
                        }
                    }
                }
            }

        }

    }

    /**
     * 处理文本 {@link InputStream}
     *
     * @param in   steam
     * @param e    处理后的参数
     * @param task 处理每一行的逻辑
     */
    public static <E> void of(InputStream in, E e, OnLine<E> task) throws Exception {

        // 当前行数 从0开始
        int index = 0;
        // 每次读取buffer大小
        byte[] buffer = new byte[Const.buf_size];
        // 读取字节数
        int readSize;
        // 暂存不足一行的内容
        byte[] noneLine = Const.EMPTY_BYTE_ARRAY;
        // 暂存内容的长度
        int noneLineLen;

        while (true) {

            readSize = in.read(buffer);
            if (readSize <= 0) {
                // 处理最后一行 不带换行符的文本
                noneLineLen = noneLine.length;
                if (noneLineLen > 0) {
                    task.handle(index, noneLine, e);
                    // 最后一行 直接break 无需增加行数
                    // index++;
                }

                break;
            }

            /* 读取一行 处理一行 */
            // 记录数组 标志
            int start = 0;
            int length;
            // 一行的内容
            byte[] oneLine;

            for (int i = 0; i < readSize; i++) {
                byte current = buffer[i];
                // 处理换行符
                if (current == (byte) '\r' || current == (byte) '\n') {
                    // 当前byte不计算在内
                    length = i - start;

                    if (start == 0) {
                        // 忽略空行
                        noneLineLen = noneLine.length;
                        if (length < 1 && noneLineLen < 1) {
                            start++;
                            continue;
                        }

                        // 兼容上一次buffer最后没有换行符的情况
                        int newLength = length;

                        if (noneLineLen > 0) {
                            newLength += noneLineLen;
                        }

                        oneLine = new byte[newLength];
                        if (noneLineLen > 0) {
                            System.arraycopy(noneLine, 0, oneLine, 0, noneLineLen);
                            System.arraycopy(buffer, start, oneLine, noneLineLen, length);

                            // 初始化noneLine
                            noneLine = Const.EMPTY_BYTE_ARRAY;
                        } else {
                            System.arraycopy(buffer, start, oneLine, 0, newLength);
                        }
                    } else {
                        // 忽略空行
                        if (length < 1) {
                            start++;
                            continue;
                        }

                        oneLine = new byte[length];
                        System.arraycopy(buffer, start, oneLine, 0, length);
                    }

                    // 处理一行的内容
                    task.handle(index, oneLine, e);
                    index++;

                    // 重新定位起始位置
                    start = i + 1;
                    // 如果当前是 '\r' 下一个是'\n' 则直接忽略
                    if (current == (byte) '\r' && (i + 1) < readSize) {
                        byte next = buffer[i + 1];
                        if (next == (byte) '\n') {
                            i++;
                            start++;
                        }
                    }
                } else {
                    if (i == readSize - 1) {
                        // 如果是最后一个字符 则直接存在noneLine中 与下一次buffer组成一行
                        // 当前byte也需要输出 则需要+1
                        length = i - start + 1;
                        // 忽略空行
                        if (length < 1) {
                            start++;
                            continue;
                        }

                        // 如果连续两次都没有换行符 则把两次内容都整合在一起
                        noneLineLen = noneLine.length;
                        if (noneLineLen > 0) {
                            // 保存上一次内容
                            byte[] tmp = new byte[noneLineLen];
                            System.arraycopy(noneLine, 0, tmp, 0, noneLineLen);

                            noneLine = new byte[length + noneLineLen];
                            // 先copy上一次内容
                            System.arraycopy(tmp, 0, noneLine, 0, noneLineLen);
                            // 再保存这一次内容
                            System.arraycopy(buffer, start, noneLine, noneLineLen, length);
                        } else {
                            noneLine = new byte[length];
                            System.arraycopy(buffer, start, noneLine, 0, length);
                        }
                    }
                }
            }
        }
    }

}
