package com.arpan.backend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "products")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int prodId;
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
