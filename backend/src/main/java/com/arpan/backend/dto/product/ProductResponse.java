package com.arpan.backend.dto.product;


import jakarta.persistence.Lob;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductResponse {
    private long prodId;
    private int stockQuantity;
    private BigDecimal price;
    private String name;
    private String description;
    private String brand;
    private String category;
    private boolean productAvailable;
    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date releaseDate;

    //Image
    private String imageName;
    private String imageType;
    // Large Object
    @Lob
    private byte[] imageData;
}
