package com.ecommerce.product.application.usecase;

import com.ecommerce.common.test.base.BaseUnitTest;
import com.ecommerce.product.application.dto.request.CreateProductRequest;
import com.ecommerce.product.application.dto.response.ProductResponse;
import com.ecommerce.product.application.mapper.ProductMapper;
import com.ecommerce.product.application.usecase.impl.CreateProductUseCaseImpl;
import com.ecommerce.product.domain.model.Product;
import com.ecommerce.product.domain.model.ProductStatus;
import com.ecommerce.product.domain.model.Category;
import com.ecommerce.product.domain.repository.CategoryRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CreateProductUseCase Tests")
class CreateProductUseCaseTest extends BaseUnitTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private CreateProductUseCaseImpl createProductUseCase;

    private CreateProductRequest request;
    private Product savedProduct;
    private ProductResponse expectedResponse;
    private UUID categoryId;

    @BeforeEach
    void setUp() {
        categoryId = UUID.randomUUID();

        request = new CreateProductRequest();
        request.setName("Test Product");
        request.setDescription("Test Description");
        request.setSku("TEST-SKU-001");
        request.setPrice(new BigDecimal("99.99"));
        request.setCategoryId(categoryId);

        // Mock category exists
        Category category = Category.builder().name("Test Category").build();
        category.setId(categoryId);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        savedProduct = Product.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .description("Test Description")
                .sku("TEST-SKU-001")
                .price(new BigDecimal("99.99"))
                .status(ProductStatus.DRAFT)
                .build();

        expectedResponse = new ProductResponse();
        expectedResponse.setId(savedProduct.getId());
        expectedResponse.setName(savedProduct.getName());
    }

    @Test
    @DisplayName("should create product successfully")
    void shouldCreateProductSuccessfully() {
        // Given
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(productMapper.toResponse(any(Product.class))).thenReturn(expectedResponse);

        // When
        ProductResponse result = createProductUseCase.execute(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Product");

        verify(productRepository).save(any(Product.class));
        verify(productMapper).toResponse(any(Product.class));
    }

    @Test
    @DisplayName("should create product with DRAFT status by default")
    void shouldCreateProductWithDraftStatus() {
        // Given
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product product = inv.getArgument(0);
            assertThat(product.getStatus()).isEqualTo(ProductStatus.DRAFT);
            return savedProduct;
        });
        when(productMapper.toResponse(any(Product.class))).thenReturn(expectedResponse);

        // When
        createProductUseCase.execute(request);

        // Then
        verify(productRepository).save(any(Product.class));
    }
}
