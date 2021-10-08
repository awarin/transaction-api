package com.alex.services;

import com.alex.exceptions.InconsistentAmountException;
import com.alex.exceptions.InvalidStatusChangeException;
import com.alex.exceptions.TransactionNotFoundException;
import com.alex.models.internal.OrderRow;
import com.alex.models.internal.Transaction;
import com.alex.models.internal.TransactionStatus;
import com.alex.models.rest.NewTransactionRequest;
import com.alex.models.rest.RestTransaction;
import com.alex.models.rest.UpdateTransactionRequest;
import com.alex.repository.OrderRowRepository;
import com.alex.repository.TransactionRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.alex.models.internal.TransactionStatus.*;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final OrderRowRepository orderRowRepository;
    private final TransactionalOperator rxtx;

    public TransactionService(TransactionRepository transactionRepository, OrderRowRepository orderRowRepository, TransactionalOperator rxtx) {
        this.transactionRepository = transactionRepository;
        this.orderRowRepository = orderRowRepository;
        this.rxtx = rxtx;
    }

    public Mono<RestTransaction> createTransaction (NewTransactionRequest transaction) {
        var totalAmount = transaction
                .getOrder()
                .stream()
                .map(o -> o.getPrice().multiply(BigDecimal.valueOf(o.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        if (totalAmount.compareTo(transaction.getAmount()) != 0) {
            return Mono.error(new InconsistentAmountException("Order amount is inconsistent with order rows."));
        }

        return rxtx.execute(tx -> transactionRepository.save(transaction.toTransaction())
                .flatMap(savedTx -> orderRowRepository
                        .saveAll(transaction.getOrder().stream().map(o -> o.toOrder(savedTx.getId())).collect(Collectors.toList()))
                        .collectList()
                        .map(orders -> formatRestTransaction(savedTx, orders))))
                .single();
    }

    public Mono<RestTransaction> getTransaction (Long id) {
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(new TransactionNotFoundException(id)))
                .flatMap(tx -> orderRowRepository.findByTransactionId(tx.getId())
                    .collectList()
                        .map(orders -> formatRestTransaction(tx, orders)));
    }

    public Mono<RestTransaction> updateTransaction (UpdateTransactionRequest updatedTx, Long id) {
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(new TransactionNotFoundException(id)))
                .<Transaction>handle((tx, synchronousSink) -> {
                    if (!tx.getVersion().equals(updatedTx.getVersion())) {
                        synchronousSink.error(new OptimisticLockingFailureException("The version sent does not match the database. Please retry"));
                    } else if (!isAuthorizedStatusChanged(tx.getStatus(), updatedTx.getStatus())) {
                        synchronousSink.error(new InvalidStatusChangeException(tx.getStatus(), updatedTx.getStatus()));
                    } else {
                        synchronousSink.next(tx);
                    }

                })
                .flatMap(tx -> transactionRepository.save(updatedTx.toTransaction(id, tx.getAmount())))
                .flatMap(savedTx -> orderRowRepository
                    .findByTransactionId(savedTx.getId())
                    .collectList()
                    .map(orders -> formatRestTransaction(savedTx, orders))
                );
    }

    public Mono<List<RestTransaction>> getAllTransactions() {
        return transactionRepository.findAll()
                .flatMap(tx -> orderRowRepository.findByTransactionId(tx.getId())
                        .collectList()
                        .map(orders -> formatRestTransaction(tx, orders)))
                .collectList();
    }

    private RestTransaction formatRestTransaction (Transaction tx, List<OrderRow> orders) {
        var restOrders = orders.stream().map(OrderRow::toRestOrderRow).collect(Collectors.toList());

        return new RestTransaction(tx.getId(), tx.getAmount(), tx.getStatus(), restOrders, tx.getPaymentType(), tx.getVersion());
    }

    private boolean isAuthorizedStatusChanged (TransactionStatus init, TransactionStatus target) {
        // TODO: ask if possible to go from NEW -> CAPTURED
        return init == target
                || (init == AUTHORIZED && target == CAPTURED)
                || (init == NEW && target == AUTHORIZED);
    }
}
