package com.example.bai4.repo;

import com.example.bai4.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findByNameContainingIgnoreCase(String q);
    
}
