package com.example.bai4.service;

import com.example.bai4.entity.Product;
import com.example.bai4.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<Product> searchByName(String q) {
        return productRepository.findByNameContainingIgnoreCase(q);
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));
    }

    public Product save(Product p) {
        return productRepository.save(p);
    }

    public void delete(Product p) {
        productRepository.delete(p);
    }
}