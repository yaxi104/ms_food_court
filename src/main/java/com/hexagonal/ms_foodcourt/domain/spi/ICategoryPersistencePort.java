package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.Category;

import java.util.Optional;

public interface ICategoryPersistencePort {

    void saveCategory(Category category);

    Optional<Category> findById(Long id);
}
