package com.hexagonal.ms_foodcourt.application.handler;

import com.hexagonal.ms_foodcourt.application.dto.request.CategoryRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.CategoryResponse;

public interface ICategoryHandler {

    void saveCategory(CategoryRequest categoryRequest);

    PaginatedResponse<CategoryResponse> findAll(Integer page, Integer size);
}
