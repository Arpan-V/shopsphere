package com.arpan.backend.service;

import com.arpan.backend.dto.product.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getAllProducts();
}
