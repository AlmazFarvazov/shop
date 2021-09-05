package ru.itis.afarvazov.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {
    private Long id;
    private Long cartId;
    private Long productId;
    private Integer amount;
    private Double price;
}
