package common.utils;

import common.utils.enums.CharSet;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author valor.
 */
public class IoKit {

    private IoKit() { }

    /**
     * default charset 'UTF-8'
     */
    public static String toString(byte[] bytes) {
        return toString(bytes, CharSet.UTF_8);
    }

    public static String toString(byte[] bytes, CharSet charset) {
        return new String(bytes, charset.getCharset());
    }

    /**
     * default charset 'UTF-8'
     */
    public static String toString(InputStream in) throws IOException {
        return toString(in, CharSet.UTF_8);
    }

    public static String toString(InputStream in, CharSet charset) throws IOException {
        return new String(toBytes(in), charset.getCharset());
    }

    public static byte[] toBytes(String str) {
        return toBytes(str, CharSet.UTF_8);
    }

    public static byte[] toBytes(String str, CharSet charset) {
        return str.getBytes(charset.getCharset());
    }

    public static byte[] toBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        write(in, out);
        byte[] bytes = out.toByteArray();
        close(out);
        return bytes;
    }

    /**
     * default charset 'UTF-8'
     */
    public static void write(String var, OutputStream out) throws IOException {
        write(var, out, CharSet.UTF_8);
    }

    public static void write(String var, OutputStream out, CharSet charset) throws IOException {
        out.write(var.getBytes(charset.getCharset()));
    }

    public static void write(byte[] var, OutputStream out) throws IOException {
        out.write(var);
    }

    public static void write(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[Const.buf_size];

        int readSize;
        while (true) {
            readSize = in.read(buffer);
            if (readSize < 0) {
                // 读取完毕
                break;
            }
            out.write(buffer, 0, readSize);

            if (readSize < Const.buf_size) {
                // 如果一次读取小于 buf_size
                // 则说明读取完毕
                break;
            }
        }
        out.flush();
    }

    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                // ignore exception
                e.printStackTrace();
            }
        }
    }
}
