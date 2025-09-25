package com.hexagonal.ms_foodcourt.domain.usecase;

import com.hexagonal.ms_foodcourt.domain.exception.CategoryAlreadyExistsException;
import com.hexagonal.ms_foodcourt.domain.model.Category;
import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import com.hexagonal.ms_foodcourt.domain.spi.ICategoryPersistencePort;
import com.hexagonal.ms_foodcourt.domain.utils.PageableHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryUseCaseTest {

    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private CategoryUseCase categoryUseCase;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("Comida rápida");
        category.setDescription("Comidas rápidas típicas");
    }

    @Test
    void saveCategoryThrowsExceptionWhenCategoryAlreadyExists() {
        when(categoryPersistencePort.findByName(category.getName()))
                .thenReturn(Optional.of(new Category()));

        assertThatThrownBy(() -> categoryUseCase.saveCategory(category))
                .isInstanceOf(CategoryAlreadyExistsException.class);

        verify(categoryPersistencePort, never()).saveCategory(any());
    }

    @Test
    void saveCategorySavesWhenCategoryDoesNotExist() {
        when(categoryPersistencePort.findByName(category.getName()))
                .thenReturn(Optional.empty());

        categoryUseCase.saveCategory(category);

        verify(categoryPersistencePort).saveCategory(category);
    }

    @Test
    void findAllReturnsPageResultFromPersistencePort() {
        int page = 0;
        int size = 5;

        PageInfo expectedPageInfo = PageableHelper.getPageable(page, size, "name");

        PageResult<Category> expectedPageResult = new PageResult<>(
                List.of(category), 1, 1L, true);

        when(categoryPersistencePort.findAll(refEq(expectedPageInfo)))
                .thenReturn(expectedPageResult);

        PageResult<Category> result = categoryUseCase.findAll(page, size);

        assertThat(result).isEqualTo(expectedPageResult);
        verify(categoryPersistencePort).findAll(refEq(expectedPageInfo));
    }
}