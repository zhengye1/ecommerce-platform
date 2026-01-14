package com.ecommerce.product.application.mapper;

import com.ecommerce.product.application.dto.request.CreateProductRequest;
import com.ecommerce.product.application.dto.response.ProductResponse;
import com.ecommerce.product.domain.model.Category;
import com.ecommerce.product.domain.model.Product;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Product entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(CreateProductRequest request);

    @Mapping(target = "category", source = "category")
    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);

    @Named("categoryToSummary")
    default ProductResponse.CategorySummary categoryToSummary(Category category) {
        if (category == null) {
            return null;
        }
        return ProductResponse.CategorySummary.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .build();
    }
}
