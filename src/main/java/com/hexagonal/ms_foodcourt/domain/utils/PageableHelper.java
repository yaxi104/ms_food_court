package com.hexagonal.ms_foodcourt.domain.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableHelper {

    private PageableHelper() {
    }

    public static Pageable getPageable(int page, int size, String ordenBy) {
        return PageRequest.of(page, size, Sort.by(ordenBy).ascending());
    }

}
