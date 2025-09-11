package com.phong.sb_ecommerce.service;

import com.phong.sb_ecommerce.model.Category;
import com.phong.sb_ecommerce.payload.request.CategoryDTO;
import com.phong.sb_ecommerce.payload.response.CategoryResponse;

public interface CategoryService {
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    public CategoryDTO createCategory(CategoryDTO categoryDTO);
    public void deleteCategory(Long categoryId);
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
    public CategoryDTO getCategoryById(Long categoryId);
}
