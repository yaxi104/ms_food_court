package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.dto.request.CategoryRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.CategoryResponse;
import com.hexagonal.ms_foodcourt.application.mapper.ICategoryMapper;
import com.hexagonal.ms_foodcourt.domain.api.ICategoryServicePort;
import com.hexagonal.ms_foodcourt.domain.model.Category;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import com.hexagonal.ms_foodcourt.util.TestDataCategoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoryHandlerTest {

    @Mock
    private ICategoryServicePort categoryServicePort;

    @Mock
    private ICategoryMapper categoryMapper;

    @InjectMocks
    private CategoryHandler categoryHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveCategoryTest() {
        CategoryRequest request = TestDataCategoryFactory.mockCategoryRequest();
        categoryHandler.saveCategory(request);

        verify(categoryMapper).toCategory(request);
    }

    @Test
    void findAllTest() {
        int page = 0;
        int size = 10;

        List<Category> categories = List.of(new Category(), new Category());
        PageResult<Category> pageResult = new PageResult<>(categories, 1, 2, true);

        when(categoryServicePort.findAll(page, size)).thenReturn(pageResult);

        List<CategoryResponse> categoryResponses = List.of(new CategoryResponse(), new CategoryResponse());
        when(categoryMapper.toCategoryResponse(categories)).thenReturn(categoryResponses);

        PaginatedResponse<CategoryResponse> response = categoryHandler.findAll(page, size);

        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo(categoryResponses);
        assertThat(response.getTotalPages()).isEqualTo(pageResult.getTotalPages());
        assertThat(response.getTotalElements()).isEqualTo(pageResult.getTotalElements());
        assertThat(response.isLast()).isEqualTo(pageResult.isLast());

        verify(categoryServicePort).findAll(page, size);
        verify(categoryMapper).toCategoryResponse(categories);
    }
}