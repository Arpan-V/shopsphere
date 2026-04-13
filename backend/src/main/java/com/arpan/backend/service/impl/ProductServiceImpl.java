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
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public ProductResponse getProductById(Long prodId) {
        Products product = productRepo.findById(prodId)
                .orElseThrow(() -> new RuntimeException("Product not found."));
        return mapToResponse(product);
    }

    @Override
    public void deleteProduct(Long prodId) {
        Products product = productRepo.findById(prodId)
                        .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepo.delete(product);
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        // Step 1: DTO request → Entity
        Products product = mapToEntity(request);

        // Step 2: Save
        Products savedProduct = productRepo.save(product);

        // Step 3: Entity → Response
        return mapToResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        // Step 1: Get existing product
        Products existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Step 2: Update fields
        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setCategory(request.getCategory());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStockQuantity(request.getStockQuantity());
        existingProduct.setProductAvailable(request.isProductAvailable());
        existingProduct.setReleaseDate(request.getReleaseDate());

        // Step 3: Update image (only if new image provided)
        MultipartFile file = request.getImage();

        if (file != null && !file.isEmpty()) {
            try {
                existingProduct.setImageName(file.getOriginalFilename());
                existingProduct.setImageType(file.getContentType());
                existingProduct.setImageData(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Image update failed");
            }
        }

        // Step 4: Save updated product
        Products updatedProduct = productRepo.save(existingProduct);

        // Step 5: Return response
        return mapToResponse(updatedProduct);
    }

    @Override
    public Products getProductEntity(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }


    private ProductResponse mapToResponse(Products product) {
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
        response.setImageUrl("/api/products/" + product.getProdId() + "/image");
        return response;
    }

    private Products mapToEntity(ProductRequest request){
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
