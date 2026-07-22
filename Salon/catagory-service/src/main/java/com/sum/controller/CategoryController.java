package com.sum.controller;

import com.sum.Model.Category;
import com.sum.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("/salon/{id}")
    public ResponseEntity<Set<Category>> getCategoriesBySalon(@PathVariable("id") Long salonId)
    {
        Set<Category> categories=categoryService.getAllCategoriesBySalonId(salonId);
        return ResponseEntity.ok(categories);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long salonId) throws Exception {
        Category category=categoryService.getCategoryById(salonId);
        return ResponseEntity.ok(category);
    }
}
