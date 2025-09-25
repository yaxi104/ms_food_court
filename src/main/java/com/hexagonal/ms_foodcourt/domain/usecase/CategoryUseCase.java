package com.hexagonal.ms_foodcourt.domain.usecase;

import com.hexagonal.ms_foodcourt.domain.api.ICategoryServicePort;
import com.hexagonal.ms_foodcourt.domain.exception.CategoryAlreadyExistsException;
import com.hexagonal.ms_foodcourt.domain.model.Category;
import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import com.hexagonal.ms_foodcourt.domain.spi.ICategoryPersistencePort;
import com.hexagonal.ms_foodcourt.domain.utils.PageableHelper;
import com.hexagonal.ms_foodcourt.domain.utils.ValidateRequest;

public class CategoryUseCase implements ICategoryServicePort {

    private final ICategoryPersistencePort categoryPersistencePort;

    public CategoryUseCase(ICategoryPersistencePort categoryPersistencePort) {
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public void saveCategory(Category category) {
        ValidateRequest.checkNotBlank(category.getName());
        ValidateRequest.checkNotBlank(category.getDescription());

        if (categoryPersistencePort.findByName(category.getName()).isPresent()) {
            throw new CategoryAlreadyExistsException();
        }
        categoryPersistencePort.saveCategory(category);
    }

    @Override
    public PageResult<Category> findAll(Integer page, Integer size) {
        PageInfo pageInfo = PageableHelper.getPageable(page, size, "name");
        return categoryPersistencePort.findAll(pageInfo);
    }
}
