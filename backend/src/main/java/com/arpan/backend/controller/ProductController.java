package com.arpan.backend.controller;

import com.arpan.backend.dto.product.ProductResponse;
import com.arpan.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/product/{prodId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable int prodId){
        return ResponseEntity.ok(productService.getProductById(prodId));
    }

    @PostMapping("/product/{prodId}")
    public ResponseEntity<String> deleteProduct(@PathVariable int prodId){
        productService.deleteProduct(prodId);
        return ResponseEntity.ok("Product deleted successfully");
    }


}
