package com.alex.controllers;

import com.alex.models.rest.NewTransactionRequest;
import com.alex.models.rest.RestTransaction;
import com.alex.models.rest.UpdateTransactionRequest;
import com.alex.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public Mono<ResponseEntity<RestTransaction>> createTransaction(@RequestBody final NewTransactionRequest newTransaction) {
        return transactionService
                .createTransaction(newTransaction)
                .map(ResponseEntity::ok);
    }

    @PutMapping(value = "/{id}")
    public Mono<ResponseEntity<RestTransaction>> updateTransaction(
            @PathVariable final Long id,
            @RequestBody final UpdateTransactionRequest updateTransactionRequest
    ) {
        return transactionService
                .updateTransaction(updateTransactionRequest, id)
                .map(ResponseEntity::ok);
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<RestTransaction>> getTransaction(@PathVariable final Long id) {
        return transactionService
                .getTransaction(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping()
    public Mono<ResponseEntity<List<RestTransaction>>> getAllTransactions() {
        return transactionService
                .getAllTransactions()
                .map(ResponseEntity::ok);
    }
}
