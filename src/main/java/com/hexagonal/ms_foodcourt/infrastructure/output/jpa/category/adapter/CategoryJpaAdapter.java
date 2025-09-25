package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.category.adapter;

import com.hexagonal.ms_foodcourt.domain.model.Category;
import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import com.hexagonal.ms_foodcourt.domain.spi.ICategoryPersistencePort;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.category.entity.CategoryEntity;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.category.mapper.ICategoryEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.category.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryPersistencePort {

    private final ICategoryRepository categoryRepository;

    private final ICategoryEntityMapper categoryEntityMapper;

    @Override
    public void saveCategory(Category category) {
        categoryRepository.save(categoryEntityMapper.toEntity(category));
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryEntityMapper::toCategory);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name)
                .map(categoryEntityMapper::toCategory);
    }

    @Override
    public PageResult<Category> findAll(PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize(), Sort.by(pageInfo.getSortBy()));
        Page<CategoryEntity> page = categoryRepository.findAll(pageable);
        List<Category> content = categoryEntityMapper.toCategoryList(page.getContent());
        return new PageResult<>(content, page.getTotalPages(), page.getTotalElements(), page.isLast());
    }
}
