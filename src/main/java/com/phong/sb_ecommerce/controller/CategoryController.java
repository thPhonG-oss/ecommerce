package com.phong.sb_ecommerce.controller;

import com.phong.sb_ecommerce.payload.request.CategoryDTO;
import com.phong.sb_ecommerce.service.CategoryService;
import com.phong.sb_ecommerce.utils.AppConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Tag(
        name = "Category APIs",
        description = "APIs for managing category"
    )
    @Operation(
        summary = "Get all categories",
        description = "API to retrieve all categories"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200",description = "Retrieve all categories successfully."),
        @ApiResponse(responseCode = "404", description = "Category is empty.", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
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


    @Tag(
        name = "Category APIs",
        description = "APIs for managing category"
    )
    @Operation(
        summary = "Create category",
        description = "API to create new category"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201",description = "Create category successfully."),
        @ApiResponse(responseCode = "401", description = "Invalid input value.", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PostMapping("/public/categories")
    public ResponseEntity<Object> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        return new ResponseEntity<>(categoryService.createCategory(categoryDTO), HttpStatus.CREATED);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<Object> updateCategory(@RequestBody CategoryDTO categoryDTO,@Parameter(description = "Category id that you wish to update") @PathVariable(name = "categoryId") Long categoryId){
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

