package com.phong.sb_ecommerce.service.impl;

import com.phong.sb_ecommerce.exception.APIException;
import com.phong.sb_ecommerce.exception.ResourcesNotFoundException;
import com.phong.sb_ecommerce.mapper.CategoryMapper;
import com.phong.sb_ecommerce.model.Category;
import com.phong.sb_ecommerce.payload.request.CategoryDTO;
import com.phong.sb_ecommerce.payload.response.CategoryResponse;
import com.phong.sb_ecommerce.repository.CategoryRepository;
import com.phong.sb_ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("ASC")?Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<Category> page = categoryRepository.findAll(pageable);
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        List<Category> categories = page.getContent();
        if(categories.isEmpty())
            throw new APIException("No Category created till now.");
        categories.forEach(category -> {categoryDTOS.add(categoryMapper.toCategoryDTO(category));});
        return new CategoryResponse(categoryDTOS,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.toCategory(categoryDTO);
        Category existCategory =  categoryRepository.findCategoryByCategoryName(category.getCategoryName());
        if(existCategory != null){
            throw new APIException("Category with name " + category.getCategoryName()  + " already exists.");
        } else {
            return categoryMapper.toCategoryDTO(categoryRepository.save(category));
        }
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourcesNotFoundException("Category", "Category Id", categoryId));

        categoryRepository.delete(category);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
//        Optional<Category> savedCategoryOptional = categoryRepository.findById(categoryId);
        Category category = categoryMapper.toCategory(categoryDTO);
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourcesNotFoundException("Category", "Category Id", categoryId));
        Category savedCategory;

        category.setCategoryId(categoryId);
        savedCategory  = categoryRepository.save(category);
        return categoryMapper.toCategoryDTO(savedCategory);
    }

    @Override
    public CategoryDTO getCategoryById(Long categoryId) {
        return null;
    }
}
