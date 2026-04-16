package com.arpan.backend.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "products")
@Builder
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prodId;

    private int stockQuantity;
    private BigDecimal price;
    private String name;
    private String description;
    private String brand;
    private String category;
    private boolean productAvailable;
    private Date releaseDate;

    //Image
    private String imageName;
    private String imageType;
    // Large Object
    @Lob
    private byte[] imageData;

    @Override
    public String toString() {
        return "Products{" +
                "prodId=" + prodId +
                ", stockQuantity=" + stockQuantity +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                ", productAvailable=" + productAvailable +
                ", releaseDate=" + releaseDate +
                ", imageName='" + imageName + '\'' +
                ", imageType='" + imageType + '\'' +
                ", imageData=" + Arrays.toString(imageData) +
                '}';
    }
}
