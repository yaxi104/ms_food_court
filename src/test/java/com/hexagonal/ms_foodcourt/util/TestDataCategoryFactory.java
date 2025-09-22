package com.hexagonal.ms_foodcourt.util;

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
        entity.setDescription("Platos para comenzar");
        return entity;
    }
}
