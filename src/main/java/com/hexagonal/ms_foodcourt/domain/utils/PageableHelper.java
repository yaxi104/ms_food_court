package com.hexagonal.ms_foodcourt.domain.utils;

import com.hexagonal.ms_foodcourt.domain.model.PageInfo;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.DEFAULT_PAGE;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.DEFAULT_SIZE;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.MAX_SIZE;

public class PageableHelper {

    private PageableHelper() {
    }

    public static PageInfo getPageable(Integer page, Integer size, String ordenBy) {
        PageInfo pageInfo = new PageInfo();
        int validPage = (page == null || page < 0) ? DEFAULT_PAGE : page;
        int validSize = (size == null || size <= 0 || size > MAX_SIZE) ? DEFAULT_SIZE : size;

        pageInfo.setPage(validPage);
        pageInfo.setSize(validSize);
        pageInfo.setSortBy(ordenBy);

        return pageInfo;
    }

}
