package ru.itis.afarvazov.dto;

import lombok.Data;
import ru.itis.afarvazov.models.Product;

@Data
public class NewProductDto {

    private String title;
    private Double price;
    private Integer availableQuantity;

    private Product.Category category;

}
