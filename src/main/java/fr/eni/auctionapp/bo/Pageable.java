package fr.eni.auctionapp.bo;

import java.util.ArrayList;
import java.util.List;

public class Pageable<T> {
    List<T> items;
    int pageIndex;
    int totalPages;
    boolean isEmpty;

    public Pageable(){
        items = new ArrayList<>();
        pageIndex = 0;
        totalPages = 0;
        isEmpty = true;
    }

    public Pageable(List<T> items, int pageIndex, int totalPages, boolean isEmpty) {
        this.items = items;
        this.pageIndex = pageIndex;
        this.totalPages = totalPages;
        this.isEmpty = isEmpty;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        this.isEmpty = empty;
    }
}
