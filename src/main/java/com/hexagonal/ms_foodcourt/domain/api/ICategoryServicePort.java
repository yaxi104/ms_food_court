package com.hexagonal.ms_foodcourt.domain.api;

import com.hexagonal.ms_foodcourt.domain.model.Category;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;

public interface ICategoryServicePort {

    void saveCategory(Category category);

    PageResult<Category> findAll(Integer page, Integer size);
}
