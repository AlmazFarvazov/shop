package ru.itis.afarvazov.dto;

import lombok.Data;

@Data
public class CartItemDto {

    private Long productId;
    private Integer amount;

}
