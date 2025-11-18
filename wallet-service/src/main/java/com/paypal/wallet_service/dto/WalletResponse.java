package com.paypal.wallet_service.dto;


import lombok.*;

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
