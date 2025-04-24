package tpp.profixer.customer.data.model.api.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private String bankInfo;
    private Boolean isUseSellCode;
    private Double couponMoney;
    private List<CartItem> cartItems;
}
