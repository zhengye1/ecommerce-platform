package com.ecommerce.product.application.usecase;

import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.common.test.base.BaseUnitTest;
import com.ecommerce.product.application.dto.response.ProductResponse;
import com.ecommerce.product.application.mapper.ProductMapper;
import com.ecommerce.product.application.usecase.impl.GetProductUseCaseImpl;
import com.ecommerce.product.domain.model.Product;
import com.ecommerce.product.domain.model.ProductStatus;
import com.ecommerce.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("GetProductUseCase Tests")
class GetProductUseCaseTest extends BaseUnitTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private GetProductUseCaseImpl getProductUseCase;

    private UUID productId;
    private Product product;
    private ProductResponse expectedResponse;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();

        product = Product.builder()
                .id(productId)
                .name("Test Product")
                .description("Test Description")
                .sku("TEST-SKU-001")
                .price(new BigDecimal("99.99"))
                .status(ProductStatus.ACTIVE)
                .build();

        expectedResponse = new ProductResponse();
        expectedResponse.setId(productId);
        expectedResponse.setName("Test Product");
    }

    @Test
    @DisplayName("should return product when found")
    void shouldReturnProductWhenFound() {
        // Given
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(expectedResponse);

        // When
        ProductResponse result = getProductUseCase.getById(productId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getName()).isEqualTo("Test Product");
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when product not found")
    void shouldThrowExceptionWhenNotFound() {
        // Given
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> getProductUseCase.getById(productId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("should return product by SKU")
    void shouldReturnProductBySku() {
        // Given
        String sku = "TEST-SKU-001";
        when(productRepository.findBySku(sku)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(expectedResponse);

        // When
        ProductResponse result = getProductUseCase.getBySku(sku);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Product");
    }
}
