package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.Category;
import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;

import java.util.Optional;

public interface ICategoryPersistencePort {

    void saveCategory(Category category);

    Optional<Category> findById(Long id);

    Optional<Category> findByName(String name);

    PageResult<Category> findAll(PageInfo pageInfo);
}
