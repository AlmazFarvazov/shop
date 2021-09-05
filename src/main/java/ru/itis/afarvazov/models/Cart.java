package ru.itis.afarvazov.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {

    private Long id;
    private Long ownerId;
    private List<CartItem> cartItems;
    private Double totalPrice;
    private Boolean active;

}
