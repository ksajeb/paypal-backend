package com.paypal.transaction_service.service;

import com.paypal.transaction_service.dto.TransferRequestDto;
import com.paypal.transaction_service.entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {
    Transaction createTransaction(Transaction transaction);

    public Transaction getTransactionById(Long id);

    public List<Transaction> getTransactionsByUser(Long userId);}
