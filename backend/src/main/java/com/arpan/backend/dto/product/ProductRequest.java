package com.arpan.backend.dto.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductRequest {

    private String name;
    private String description;
    private String brand;
    private String category;
    private BigDecimal price;
    private int stockQuantity;
    private boolean productAvailable;
    private Date releaseDate;

    private MultipartFile image;
}
