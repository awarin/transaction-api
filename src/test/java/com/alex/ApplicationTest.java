package com.alex;

import com.alex.models.internal.PaymentType;
import com.alex.models.internal.TransactionStatus;
import com.alex.models.rest.NewTransactionRequest;
import com.alex.models.rest.RestOrderRow;
import com.alex.models.rest.RestTransaction;
import com.alex.models.rest.UpdateTransactionRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void itShouldReturnAnEmptyListOfTransactions() {
        var transactionUri = "http://localhost:" + port + "/transaction";
        var response = restTemplate.getForObject(transactionUri, RestTransaction[].class);
        assertThat(response, Matchers.arrayWithSize(0));
    }

    @Test
    public void itShouldFollowScenario() {
        var transactionUri = "http://localhost:" + port + "/transaction";
        // Given a transaction is added to the system
        var orderRows = new ArrayList<RestOrderRow>();
        orderRows.add(new RestOrderRow(4, BigDecimal.valueOf(10), "Paire de gants"));
        orderRows.add(new RestOrderRow(1, BigDecimal.valueOf(14.8), "Bonnet en laine"));

        // When the endpoint is called
        var transaction = new NewTransactionRequest(BigDecimal.valueOf(54.8), orderRows, PaymentType.CREDIT_CARD);
        var response = restTemplate
                .postForObject(transactionUri, transaction, RestTransaction.class);
        // Then the resulting transaction should be returned
        assertThat(response.getId(), Matchers.is(1L));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // When changing the status of the order to AUTHORIZED
        var updateRequest = new UpdateTransactionRequest(PaymentType.PAYPAL, response.getVersion(), TransactionStatus.AUTHORIZED);
        var entity = new HttpEntity<>(updateRequest, headers);
        var updateResponse = restTemplate
                .exchange(transactionUri + "/1", HttpMethod.PUT, entity, RestTransaction.class);
        // Then the update should be performed
        assertThat(updateResponse.getBody().getStatus(), Matchers.is(TransactionStatus.AUTHORIZED));

        // When changing the status to CAPTURED
        updateRequest = new UpdateTransactionRequest(PaymentType.PAYPAL, updateResponse.getBody().getVersion(), TransactionStatus.CAPTURED);
        entity = new HttpEntity<>(updateRequest, headers);
        updateResponse = restTemplate
                .exchange(transactionUri + "/1", HttpMethod.PUT, entity, RestTransaction.class);

        // Then the update should be performed
        assertThat(updateResponse.getBody().getStatus(), Matchers.is(TransactionStatus.CAPTURED));

        // When adding a new transaction
        orderRows = new ArrayList<RestOrderRow>();
        orderRows.add(new RestOrderRow(1, BigDecimal.valueOf(208), "Vélo"));

        transaction = new NewTransactionRequest(BigDecimal.valueOf(208), orderRows, PaymentType.PAYPAL);
        response = restTemplate
                .postForObject(transactionUri, transaction, RestTransaction.class);
        // Then the resulting transaction should be returned
        assertThat(response.getId(), Matchers.is(2L));

        // When querying the list of transactions
        var allTransactions = restTemplate.getForObject(transactionUri, RestTransaction[].class);

        // Then we should receive 2 transactions
        assertThat(allTransactions, Matchers.arrayWithSize(2));
    }
}
