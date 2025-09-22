package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.category.repository;

import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.category.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
