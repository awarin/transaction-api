package com.alex.repository;

import com.alex.models.internal.OrderRow;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderRowRepository extends ReactiveCrudRepository<OrderRow, Long> {

    @Query("SELECT * FROM order_row where transaction_id = :id")
    Flux<OrderRow> findByTransactionId(Long id);

    @Query("DELETE FROM order_row where transaction_id = :id")
    Flux<OrderRow> deleteByTransactionId(Long id);
}
