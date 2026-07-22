package com.sum.controller;

import com.sum.Model.Category;
import com.sum.dto.SalonDTO;
import com.sum.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
@RestController
@RequestMapping("/api/categories/salon-owner")
@RequiredArgsConstructor
public class SalonCategoryController {
    private final CategoryService categoryService;
        @PostMapping()
        public ResponseEntity<Category>createCategory(@RequestBody Category category) throws Exception
        {
            SalonDTO salonDTO=new SalonDTO();
            salonDTO.setId(1L);
            Category savedCategory=categoryService.saveCategory(category,salonDTO);
            return ResponseEntity.ok(savedCategory);
        }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long id) throws Exception
    {
        SalonDTO salonDTO=new SalonDTO();
        salonDTO.setId(1L);
       categoryService.deleteCategoryById(id,salonDTO.getId());
        return ResponseEntity.ok("Category Deleted Successfully");
    }
}
