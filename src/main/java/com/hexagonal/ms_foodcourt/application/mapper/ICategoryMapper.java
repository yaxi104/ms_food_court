package com.hexagonal.ms_foodcourt.application.mapper;


import com.hexagonal.ms_foodcourt.application.dto.request.CategoryRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.CategoryResponse;
import com.hexagonal.ms_foodcourt.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICategoryMapper {

    Category toCategory(CategoryRequest categoryRequest);

    List<CategoryResponse> toCategoryResponse(List<Category> category);

}
