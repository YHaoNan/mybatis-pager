package top.yudoge.mybatis.interceptors.pager.strategies;

import org.apache.ibatis.session.RowBounds;

public class PagerHelper {
    private static final ThreadLocal<RowBounds> threadLocal = new ThreadLocal<>();

    public static void startPage(int pageNum, int pageSize) {
        threadLocal.set(new RowBounds(pageNum - 1, pageSize));
    }


    public static RowBounds getPage() {
        return threadLocal.get();
    }

    public static boolean isPaged() {
        return threadLocal.get() != null;
    }

    public static void clearPage() {
        threadLocal.remove();
    }
}
