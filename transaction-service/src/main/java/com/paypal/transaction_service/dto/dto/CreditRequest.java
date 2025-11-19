package com.paypal.transaction_service.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditRequest {
    private Long userId;
    private String currency="INR";
    private Long amount;
}
