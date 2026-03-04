package com.example.bai4.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root;

    public FileStorageService(@Value("${app.upload.dir}") String uploadDir) {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.root);
        } catch (IOException e) {
            throw new RuntimeException("Không tạo được thư mục upload: " + this.root, e);
        }
    }

    public String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        String original = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
        String ext = "";
        int dot = original.lastIndexOf(".");
        if (dot >= 0) ext = original.substring(dot);

        String safeName = UUID.randomUUID() + ext; // tránh trùng
        try {
            Path target = root.resolve(safeName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return safeName;
        } catch (IOException e) {
            throw new RuntimeException("Lưu ảnh thất bại", e);
        }
    }

    public void deleteIfExists(String imageName) {
        if (imageName == null || imageName.isBlank()) return;
        try {
            Files.deleteIfExists(root.resolve(imageName));
        } catch (IOException ignored) { }
    }
}
