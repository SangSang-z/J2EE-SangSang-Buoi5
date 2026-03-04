package com.example.bai4.seed;

import com.example.bai4.entity.Category;
import com.example.bai4.repo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        categoryRepository.findByNameIgnoreCase("Điện thoại")
                .orElseGet(() -> categoryRepository.save(new Category(null, "Điện thoại")));
        categoryRepository.findByNameIgnoreCase("Laptop")
                .orElseGet(() -> categoryRepository.save(new Category(null, "Laptop")));
    }
}
