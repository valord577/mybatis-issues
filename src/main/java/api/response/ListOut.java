package api.response;

import java.util.List;

/**
 * @author valor.
 */
public class ListOut<T> {

    private int total;
    private List<T> list;

    public static <T> ListOut<T> create() {
        return new ListOut<>();
    }

    public ListOut<T> setTotal(int total) {
        this.total = total;
        return this;
    }

    public int getTotal() {
        if (this.total == 0) {
            if (null == list || list.isEmpty()) {
                return 0;
            }

            this.total = list.size();
        }
        return this.total;
    }

    public ListOut<T> setList(List<T> list) {
        this.list = list;
        return this;
    }

    public List<T> getList() {
        return list;
    }
}
