package com.hexagonal.ms_foodcourt.util;

import com.hexagonal.ms_foodcourt.application.dto.request.CategoryRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.CategoryResponse;
import com.hexagonal.ms_foodcourt.domain.model.Category;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.category.entity.CategoryEntity;

public class TestDataCategoryFactory {

    public static Category mockCategory() {
        return new Category(1L, "Entradas", "Platos para comenzar");
    }

    public static CategoryEntity mockCategoryEntity() {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(1L);
        entity.setName("Entradas");
        entity.setDescripcion("Descripcion");
        return entity;
    }

    public static CategoryRequest mockCategoryRequest() {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("Entradas");
        categoryRequest.setDescription("Descripcion");
        return categoryRequest;
    }

    public static CategoryResponse mockCategoryResponse() {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(1L);
        categoryResponse.setName("Entradas");
        categoryResponse.setDescription("Descripcion");
        return categoryResponse;
    }

}
