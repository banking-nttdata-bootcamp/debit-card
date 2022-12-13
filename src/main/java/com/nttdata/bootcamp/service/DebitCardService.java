package com.nttdata.bootcamp.service;

import com.nttdata.bootcamp.entity.DebitCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Interface Service
public interface DebitCardService {
    Flux<DebitCard> findAllDebitCard();
    Flux<DebitCard> findAllDebitCardsByCustomer(String dni);

    Flux<DebitCard> findAccountsByDebitCard(String debitCardNumber);

    Mono<DebitCard> findMainAccountsByDebitCard(String debitCardNumber);

    Mono<DebitCard> findDebitCardByAccount(String accountNumber);

    Mono<DebitCard> saveDebitCard(DebitCard dataDebitCard, Boolean main);

    Mono<DebitCard> updateMainDebitCard(DebitCard dataDebitCard);

}
