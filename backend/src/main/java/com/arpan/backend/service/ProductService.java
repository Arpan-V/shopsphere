package com.arpan.backend.service;

import com.arpan.backend.dto.product.ProductRequest;
import com.arpan.backend.dto.product.ProductResponse;
import com.arpan.backend.entity.Products;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long prodId);

    void deleteProduct(Long prodId);

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductRequest request);

    Products getProductEntity(Long id);
}
