package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.dto.request.CategoryRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.CategoryResponse;
import com.hexagonal.ms_foodcourt.application.handler.ICategoryHandler;
import com.hexagonal.ms_foodcourt.application.mapper.ICategoryMapper;
import com.hexagonal.ms_foodcourt.domain.api.ICategoryServicePort;
import com.hexagonal.ms_foodcourt.domain.model.Category;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryHandler implements ICategoryHandler {

    private final ICategoryServicePort categoryServicePort;
    private final ICategoryMapper categoryMapper;

    @Override
    public void saveCategory(CategoryRequest categoryRequest) {
        categoryMapper.toCategory(categoryRequest);
    }

    @Override
    public PaginatedResponse<CategoryResponse> findAll(Integer page, Integer size) {
        PageResult<Category> resultPage = categoryServicePort.findAll(page, size);
        List<CategoryResponse> categoryResponses = categoryMapper.toCategoryResponse(resultPage.getContent());
        return new PaginatedResponse<>(
                categoryResponses,
                resultPage.getTotalPages(),
                resultPage.getTotalElements(),
                resultPage.isLast()
        );
    }
}
