package tpp.profixer.customer.data.model.api.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private String id;
    private Integer status;
    private Expert student;
    private Double totalMoney;
    private Double couponMoney;
    private Double selloffMoney;
    private Integer state;
    private Integer PaymentMethod;
    private String paymentData;
    private String payoutStatus;
    private String code;
    private List<Transaction> transactions;

    public double originalPrice(){
        double price = totalMoney;
        if(couponMoney != null) price = price + couponMoney;
        if(selloffMoney != null) price = price + selloffMoney;
        return price;
    }
}
