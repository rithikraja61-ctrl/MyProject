package com.sum.service;

import com.sum.Model.Category;
import com.sum.dto.SalonDTO;

import java.util.Set;

public interface CategoryService {
    Category saveCategory(Category category, SalonDTO salonDTO);

    Set<Category> getAllCategoriesBySalonId(Long salonId);

    Category getCategoryById(Long categoryId) throws Exception;
    void deleteCategoryById(Long categoryId,Long salonId) throws Exception;
}
