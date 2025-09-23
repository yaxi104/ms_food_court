package com.hexagonal.ms_foodcourt.domain.model;

public class PageInfo {
    private Integer page;
    private Integer size;
    private String sortBy;

    public PageInfo(Integer page, Integer size, String sortBy) {
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
    }

    public PageInfo() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}