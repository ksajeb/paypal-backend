package com.paypal.transaction_service.service;

import com.paypal.transaction_service.dto.TransferRequestDto;
import com.paypal.transaction_service.entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {

    Transaction createTransaction(TransferRequestDto requestDto);

    List<Transaction> getAllTransaction();
}
