package com.arpan.backend.dto.product;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductResponse {

    private Long prodId;
    private String name;
    private String description;
    private String brand;
    private String category;
    private BigDecimal price;
    private int stockQuantity;
    private boolean productAvailable;
    private Date releaseDate;
    private String imageUrl;
}
