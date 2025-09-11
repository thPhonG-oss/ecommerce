package com.phong.sb_ecommerce.repository;

import com.phong.sb_ecommerce.model.Category;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    public Boolean findCategoryByCategoryId(Long categoryId);
    public void deleteCategoryByCategoryId(Long categoryId);

    Category findCategoryByCategoryName(@NotBlank(message = "Category name must be not blank") String categoryName);
}
