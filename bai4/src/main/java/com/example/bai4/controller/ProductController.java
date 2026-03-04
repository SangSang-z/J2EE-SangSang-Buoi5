package com.example.bai4.controller;

import com.example.bai4.dto.ProductForm;
import com.example.bai4.entity.Category;
import com.example.bai4.entity.Product;
import com.example.bai4.service.CategoryService;
import com.example.bai4.service.FileStorageService;
import com.example.bai4.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public String list(@RequestParam(name = "q", required = false) String q, Model model) {
        List<Product> products = (q == null || q.isBlank())
                ? productService.getAll()
                : productService.searchByName(q);

        model.addAttribute("products", products);
        model.addAttribute("q", q == null ? "" : q);
        return "products";
    }

    @GetMapping("/create")
    public String showCreate(Model model) {
        model.addAttribute("form", new ProductForm());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("mode", "create");
        return "product_form";
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("form") ProductForm form,
            BindingResult br,
            @RequestParam("imageFile") MultipartFile imageFile,
            Model model) {

        if (imageFile == null || imageFile.isEmpty()) {
            br.rejectValue("imageName", "image.required", "Vui lòng chọn ảnh");
        } else {
            String originalFilename = imageFile.getOriginalFilename();
            if (originalFilename != null && originalFilename.length() > 200) {
                br.rejectValue("imageName", "image.tooLong", "Tên hình ảnh không quá 200 ký tự");
            }
        }

        if (br.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("mode", "create");
            return "product_form";
        }

        Category category = categoryService.getById(form.getCategoryId());
        String savedImageName = fileStorageService.saveImage(imageFile);

        Product product = new Product();
        product.setName(form.getName());
        product.setPrice(form.getPrice());
        product.setCategory(category);
        product.setImageName(savedImageName);

        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model) {
        Product p = productService.getById(id);

        ProductForm form = new ProductForm();
        form.setId(p.getId());
        form.setName(p.getName());
        form.setPrice(p.getPrice());
        form.setCategoryId(p.getCategory().getId());
        form.setImageName(p.getImageName());

        model.addAttribute("form", form);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("mode", "edit");
        model.addAttribute("currentImage", p.getImageName());
        return "product_form";
    }

    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute("form") ProductForm form,
                       BindingResult br,
                       @RequestParam("imageFile") MultipartFile imageFile,
                       Model model) {

        Product p = productService.getById(form.getId());

        if (br.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("mode", "edit");
            model.addAttribute("currentImage", p.getImageName());
            return "product_form";
        }

        Category category = categoryService.getById(form.getCategoryId());

        if (imageFile != null && !imageFile.isEmpty()) {
            String newName = fileStorageService.saveImage(imageFile);

            if (newName != null && newName.length() > 200) {
                br.rejectValue("imageName", "image.tooLong", "Tên hình ảnh không quá 200 ký tự");
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("mode", "edit");
                model.addAttribute("currentImage", p.getImageName());
                return "product_form";
            }

            fileStorageService.deleteIfExists(p.getImageName());
            p.setImageName(newName);
        }

        p.setName(form.getName());
        p.setPrice(form.getPrice());
        p.setCategory(category);

        productService.save(p);
        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Product p = productService.getById(id);
        fileStorageService.deleteIfExists(p.getImageName());
        productService.delete(p);
        return "redirect:/products";
    }
}