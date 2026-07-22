package com.sum.service.impl;

import com.sum.Model.Category;
import com.sum.dto.SalonDTO;
import com.sum.repository.CategoryRepository;
import com.sum.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
   private final CategoryRepository categoryRepository;
    @Override
    public Category saveCategory(Category category, SalonDTO salonDTO) {
        Category newCategory =new Category();
        newCategory.setName(category.getName());
        newCategory.setSalonId(salonDTO.getId());
        newCategory.setImage(category.getImage());
        return categoryRepository.save(newCategory);
    }

    @Override
    public Set<Category> getAllCategoriesBySalonId(Long salonId) {
        return categoryRepository.findBySalonId(salonId);
    }

    @Override
    public Category getCategoryById(Long categoryId) throws Exception {
        Category category=categoryRepository.findById(categoryId).orElse(null);
        if(category==null)
        {
            throw new Exception("Category not found");
        }
        return category;
    }

    @Override
    public void deleteCategoryById(Long categoryId,Long salonId) throws Exception {
       Category category=getCategoryById(categoryId);
       if(!category.getSalonId().equals(salonId))
       {
           throw new Exception("You Does not Have Permission to delete this category");
       }
       categoryRepository.deleteById(categoryId);
    }
}
