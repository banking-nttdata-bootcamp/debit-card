package com.nttdata.bootcamp.service.impl;

import com.nttdata.bootcamp.entity.DebitCard;
import com.nttdata.bootcamp.repository.DebitCardRepository;
import com.nttdata.bootcamp.service.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Service implementation
@Service
public class DebitCardServiceImpl implements DebitCardService {

    @Autowired
    private DebitCardRepository debitCardRepository;

    @Override
    public Flux<DebitCard> findAllDebitCard() {
        Flux<DebitCard> debitCardFlux = debitCardRepository
                .findAll();
        return debitCardFlux;
    }

    @Override
    public Flux<DebitCard> findAccountsByDebitCard(String debitCardNumber) {
        Flux<DebitCard> debitCardFlux = debitCardRepository
                .findAll()
                .filter(x -> x.getDebitCardNumber().equals(debitCardNumber));
        return debitCardFlux;
    }

    @Override
    public Mono<DebitCard> findMainAccountsByDebitCard(String debitCardNumber) {
        Mono<DebitCard> debitCardMono = debitCardRepository
                .findAll()
                .filter(x -> x.getDebitCardNumber().equals(debitCardNumber)&& x.getMainAccount())
                .next();
        return debitCardMono;
    }
    @Override
    public Mono<DebitCard> findDebitCardByAccount(String accountNumber) {
        Mono<DebitCard> debitCardMono = debitCardRepository
                .findAll()
                .filter(x -> x.getAccountNumber().equals(accountNumber))
                .next();
        return debitCardMono;
    }
    @Override
    public Mono<DebitCard> saveDebitCard(DebitCard dataDebitCard) {
       /* Mono<DebitCard> debitCardMono = Mono.empty();
        Flux<DebitCard> cardFlux= findAccountsByDebidCard(dataDebitCard.getDebitCardNumber());
        if(cardFlux.toStream().)
        dataDebitCard.setMainAccount(true);
        dataDebitCard.setOrderAssociation(true);



        dataDebitCard.setCommissionMaintenance(0);
        dataDebitCard.setMovementsMonthly(true);
        dataDebitCard.setSaving(true);
        dataDebitCard.setCurrentAccount(false);
        dataDebitCard.setFixedTerm(false);
        debitCardMono = debitCardRepository.save(dataSavingAccount);
        return passive
                .flatMap(__ -> Mono.<DebitCard>error(new Error("El cliente con dni " + dataSavingAccount.getDni() + " YA TIENE UNA CUENTA")))
                .switchIfEmpty(debitCardRepository.save(dataSavingAccount));*/
        return debitCardRepository.save(dataDebitCard);
    }

    @Override
    public Mono<DebitCard> updateDebitCard(DebitCard dataSavingAccount) {
        Mono<DebitCard> debitCardMono = findDebitCardByAccount(dataSavingAccount.getAccountNumber());
        try{
            DebitCard debitCard = debitCardMono.block();
            debitCard.setMainAccount(true);
            debitCard.setModificationDate(dataSavingAccount.getModificationDate());
            return debitCardRepository.save(debitCard);
        }catch (Exception e){
            return Mono.<DebitCard>error(new Error("The account " + dataSavingAccount.getAccountNumber() + " does not exists"));
        }
    }



}
