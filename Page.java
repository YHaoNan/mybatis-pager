package top.yudoge.mybatis.interceptors.pager;

import java.util.ArrayList;
import java.util.List;

public class Page extends ArrayList {
    // 页号
    private Integer pageNum;
    // 页大小
    private Integer pageSize;
    // 本页条目数
    private Integer pageItemCount;
    // 总条目数
    private Integer totCount;
    // 总页数
    private Integer totPageCount;
    // 是否有下一页
    private Boolean hasNext;
    // 是否有上一页
    private Boolean hasPrev;
    // 是否是首页
    private Boolean isFirstPage;
    // 是否是最后一页
    private Boolean isLastPage;

    public Page(Integer pageNum, Integer pageSize, Integer totCount, List list) {
        // 实际上应该让本类委托list，而不是这样实现，但我比较懒，这样实现会导致（如果使用缓存的话）系统中有大量的垃圾对象
        // 即使没有缓存也徒增垃圾回收器的压力
        addAll(list);
        boolean isLastPageIncomplete = totCount % pageSize != 0;

        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pageItemCount = list.size();
        this.totCount = totCount;
        this.totPageCount = totCount / pageSize + (isLastPageIncomplete ? 1 : 0);
        this.hasNext = (pageNum != totPageCount);
        this.hasPrev = (pageNum != 1);
        this.isFirstPage = !hasPrev;
        this.isLastPage = !hasNext;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageItemCount() {
        return pageItemCount;
    }

    public void setPageItemCount(Integer pageItemCount) {
        this.pageItemCount = pageItemCount;
    }

    public Integer getTotCount() {
        return totCount;
    }

    public void setTotCount(Integer totCount) {
        this.totCount = totCount;
    }

    public Integer getTotPageCount() {
        return totPageCount;
    }

    public void setTotPageCount(Integer totPageCount) {
        this.totPageCount = totPageCount;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Boolean getHasPrev() {
        return hasPrev;
    }

    public void setHasPrev(Boolean hasPrev) {
        this.hasPrev = hasPrev;
    }

    public Boolean getFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(Boolean firstPage) {
        isFirstPage = firstPage;
    }

    public Boolean getLastPage() {
        return isLastPage;
    }

    public void setLastPage(Boolean lastPage) {
        isLastPage = lastPage;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", pageItemCount=" + pageItemCount +
                ", totCount=" + totCount +
                ", totPageCount=" + totPageCount +
                ", hasNext=" + hasNext +
                ", hasPrev=" + hasPrev +
                ", isFirstPage=" + isFirstPage +
                ", isLastPage=" + isLastPage +
                '}';
    }
}
