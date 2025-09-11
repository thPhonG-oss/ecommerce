package com.phong.sb_ecommerce.mapper;

import com.phong.sb_ecommerce.model.Category;
import com.phong.sb_ecommerce.payload.request.CategoryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toCategoryDTO(Category category);
    Category toCategory(CategoryDTO categoryDTO);
}
