package com.arpan.backend.repository;

import com.arpan.backend.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Products, Integer> {

}
