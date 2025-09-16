package com.phong.sb_ecommerce.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDTO {
    @Schema(description = "Category id for a particular category", examples = {"100", "250", "104"})
    Long categoryId;
    @Schema(description = "Category name of a particular category", examples = {"Beauty", "Sporty", "Health Care"})
    String categoryName;
}
