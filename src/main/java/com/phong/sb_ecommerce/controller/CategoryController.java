package com.phong.sb_ecommerce.controller;

import com.phong.sb_ecommerce.payload.request.CategoryDTO;
import com.phong.sb_ecommerce.service.CategoryService;
import com.phong.sb_ecommerce.utils.AppConstant;
import jakarta.validation.Valid;
//import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories") // or @RequestMapping(value="/api/public/categories, method=RequestMethod.GET)
    public ResponseEntity<Object> getAllCategories(
            @RequestParam(name = "pageNumber",required = false ,defaultValue = AppConstant.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstant.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstant.SORT_CATEGORY_BY) String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstant.SORT_DIR) String sortOrder
    ) {
        if(pageNumber < 0) pageNumber = 0;
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }


    @PostMapping("/public/categories")
    public ResponseEntity<Object> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        return new ResponseEntity<>(categoryService.createCategory(categoryDTO), HttpStatus.CREATED);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<Object> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable(name = "categoryId") Long categoryId){
        return new ResponseEntity<>(categoryService.updateCategory(categoryDTO, categoryId),HttpStatus.OK);
    }

    @DeleteMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable(name = "categoryId") Long categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("Delete Successfully!", HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}")
    public ResponseEntity<Object> getCategoryById(@PathVariable(name = "categoryId") Long categoryId){
        return new ResponseEntity<>(categoryService.getCategoryById(categoryId), HttpStatus.OK);
    }

}

