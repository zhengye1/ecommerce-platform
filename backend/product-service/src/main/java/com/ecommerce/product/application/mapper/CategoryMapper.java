package com.ecommerce.product.application.mapper;

import com.ecommerce.product.application.dto.request.CreateCategoryRequest;
import com.ecommerce.product.application.dto.response.CategoryResponse;
import com.ecommerce.product.domain.model.Category;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MapStruct mapper for Category entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CreateCategoryRequest request);

    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "parentName", source = "parent.name")
    @Mapping(target = "children", expression = "java(mapChildren(category))")
    @Mapping(target = "fullPath", expression = "java(category.getFullPath())")
    CategoryResponse toResponse(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);

    default List<CategoryResponse.CategorySummary> mapChildren(Category category) {
        if (category.getChildren() == null || category.getChildren().isEmpty()) {
            return List.of();
        }
        return category.getChildren().stream()
                .map(child -> CategoryResponse.CategorySummary.builder()
                        .id(child.getId())
                        .name(child.getName())
                        .slug(child.getSlug())
                        .build())
                .collect(Collectors.toList());
    }
}
