package com.phong.sb_ecommerce.repository;

import com.phong.sb_ecommerce.model.Category;
import com.phong.sb_ecommerce.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    public Page<Product> findAllByCategory(Category category, Pageable pageable);
    public List<Product> findByCategoryOrderByPriceAsc(Category category);
    public List<Product> findByProductNameLikeIgnoreCase(String keyword);

    Product findByProductName(@NotBlank(message = "Product name can not be blank.") @Size(min = 3, message = "Product name must be at least 3 character.") String productName);

    Page<Product> findAllByProductNameLikeIgnoreCase(String s, Pageable pageable);

    Product findProductByProductId(Long productId);
}
