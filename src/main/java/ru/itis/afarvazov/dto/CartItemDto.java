package ru.itis.afarvazov.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.afarvazov.models.CartItem;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {

    private Long productId;
    private Integer amount;

    public static CartItemDto from(CartItem cartItem) {
        return CartItemDto.builder()
                .productId(cartItem.getProductId())
                .amount(cartItem.getAmount())
                .build();
    }

    public static List<CartItemDto> from(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItemDto::from)
                .collect(Collectors.toList());
    }

}
