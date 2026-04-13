package com.arpan.backend.service.impl;

import com.arpan.backend.dto.product.ProductRequest;
import com.arpan.backend.dto.product.ProductResponse;
import com.arpan.backend.entity.Products;
import com.arpan.backend.repository.ProductRepo;
import com.arpan.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Products> products = productRepo.findAll();

        return products.stream()
                .map(this::mapForRes)
                .toList();
    }


    @Override
    public ProductResponse getProductById(int prodId) {
        Products product = productRepo.findById(prodId)
                .orElseThrow(() -> new RuntimeException("Product not found."));
        return mapForRes(product);
    }

    @Override
    public void deleteProduct(int prodId) {
        Products product = productRepo.findById(prodId)
                        .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepo.delete(product);
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        // Step 1: DTO → Entity
        Products product = mapForReq(request);

        // Step 2: Save
        Products savedProduct = productRepo.save(product);

        // Step 3: Entity → Response
        return mapForRes(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(int id, ProductRequest request) {
        return null;
    }


    private ProductResponse mapForRes(Products product) {
        ProductResponse response = new ProductResponse();

        response.setProdId(product.getProdId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setBrand(product.getBrand());
        response.setCategory(product.getCategory());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setProductAvailable(product.isProductAvailable());
        response.setReleaseDate(product.getReleaseDate());

        // image URL (important)
        response.setImageUrl("/products/" + product.getProdId() + "/image");

        return response;
    }

    private Products mapForReq(ProductRequest request){
        Products product = new Products();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setProductAvailable(request.isProductAvailable());
        product.setReleaseDate(request.getReleaseDate());

        MultipartFile file = request.getImage();

        if (file != null && !file.isEmpty()) {
            try {
                product.setImageName(file.getOriginalFilename());
                product.setImageType(file.getContentType());
                product.setImageData(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed");
            }
        }

        return product;
    }
}
