package ru.itis.afarvazov.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    private Long id;
    private String title;
    private Double price;
    private Integer availableQuantity;

    private Category category;

    public enum Category {
        FOOD, ELECTRONICS, CLOTHES, SPORT
    }

}
