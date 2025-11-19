package com.paypal.transaction_service.dto.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponse {
    private Long id;
    private Long userId;
    private String currency="INR";
    private Long balance=0L;
    private Long availableBalance=0L;
}
