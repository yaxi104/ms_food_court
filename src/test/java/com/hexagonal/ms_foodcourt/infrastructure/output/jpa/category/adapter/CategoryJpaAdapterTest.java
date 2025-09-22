package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.category.adapter;

import com.hexagonal.ms_foodcourt.domain.model.Category;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.category.entity.CategoryEntity;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.category.mapper.ICategoryEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.category.repository.ICategoryRepository;
import com.hexagonal.ms_foodcourt.util.TestDataCategoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryJpaAdapterTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @Mock
    private ICategoryEntityMapper categoryEntityMapper;

    private CategoryJpaAdapter categoryJpaAdapter;

    @BeforeEach
    void setUp() {
        categoryJpaAdapter = new CategoryJpaAdapter(categoryRepository, categoryEntityMapper);
    }

    @Test
    void saveCategorySuccess() {
        Category category = TestDataCategoryFactory.mockCategory();
        CategoryEntity categoryEntity = TestDataCategoryFactory.mockCategoryEntity();

        when(categoryEntityMapper.toEntity(category)).thenReturn(categoryEntity);

        categoryJpaAdapter.saveCategory(category);

        verify(categoryEntityMapper).toEntity(category);
        verify(categoryRepository).save(categoryEntity);
    }

    @Test
    void findByIdWhenFound() {
        Long categoryId = 1L;
        CategoryEntity entity = TestDataCategoryFactory.mockCategoryEntity();
        Category domainCategory = TestDataCategoryFactory.mockCategory();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(entity));
        when(categoryEntityMapper.toCategory(entity)).thenReturn(domainCategory);

        Optional<Category> result = categoryJpaAdapter.findById(categoryId);

        assertTrue(result.isPresent());
        assertEquals(domainCategory, result.get());
        verify(categoryRepository).findById(categoryId);
        verify(categoryEntityMapper).toCategory(entity);
    }

    @Test
    void findByIdEmpty() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Optional<Category> result = categoryJpaAdapter.findById(categoryId);

        assertTrue(result.isEmpty());
        verify(categoryRepository).findById(categoryId);
        verify(categoryEntityMapper, never()).toCategory(any());
    }
}