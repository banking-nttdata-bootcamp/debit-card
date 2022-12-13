package com.nttdata.bootcamp.controller;

import com.nttdata.bootcamp.entity.DebitCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.nttdata.bootcamp.service.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Date;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/debit-card")
public class DebitCardController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DebitCardController.class);
	@Autowired
	private DebitCardService debitCardService;

	//All DebitCard Registered
	@GetMapping("/findAllDebitCards")
	public Flux<DebitCard> findAllSavingsAccounts() {
		Flux<DebitCard> debitCardFlux = debitCardService.findAllDebitCard();
		LOGGER.info("All Debits cards Registered: " + debitCardFlux);
		return debitCardFlux;
	}

	@GetMapping("/findAllDebitCardsByCustomer/{dni}")
	public Flux<DebitCard> findAllDebitCardsByCustomer(@PathVariable("dni") String dni) {
		Flux<DebitCard> debitCardFlux = debitCardService.findAllDebitCardsByCustomer(dni);
		LOGGER.info("All Debits cards Registered by customer "+dni+": " + debitCardFlux);
		return debitCardFlux;
	}

	//Account Registered by debit card
	@GetMapping("/findAccountsByDebitCard/{debitCardNumber}")
	public Flux<DebitCard> findAccountsByDebitCard(@PathVariable("debitCardNumber") String debitCardNumber) {
		Flux<DebitCard> debitCardFlux = debitCardService.findAccountsByDebitCard(debitCardNumber);
		LOGGER.info("Account Registered by debit card: "+debitCardNumber +"-" + debitCardFlux);
		return debitCardFlux;
	}

	//Search main Account by Credit Card
	//@CircuitBreaker(name = "passive", fallbackMethod = "fallBackGetSaving")
	@GetMapping("/findMainAccountsByDebitCard/{accountNumber}")
	public Mono<DebitCard> findMainAccountsByDebitCard(@PathVariable("debitCardNumber") String debitCardNumber) {
		LOGGER.info("Searching main Account Products by debit card: " + debitCardNumber);
		return debitCardService.findMainAccountsByDebitCard(debitCardNumber);
	}


	//Save Debit Card
	//@CircuitBreaker(name = "passive", fallbackMethod = "fallBackGetSaving")
	@PostMapping(value = "/saveDebitCard")
	public Mono<DebitCard> saveDebitCard(@RequestBody DebitCard debitCard){

		DebitCard dataDebit = new DebitCard();
		Mono.just(dataDebit).doOnNext(t -> {
			t.setDni(debitCard.getDni());
			t.setTypeCustomer(debitCard.getTypeCustomer());
			t.setAccountNumber(debitCard.getAccountNumber());
			t.setDebitCardNumber(debitCard.getDebitCardNumber());
			t.setStatus("active");
			t.setCreationDate(new Date());
			t.setModificationDate(new Date());
		}).onErrorReturn(dataDebit).onErrorResume(e -> Mono.just(dataDebit))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<DebitCard> passiveMono = debitCardService.saveDebitCard(dataDebit,true);
		return passiveMono;
	}
	@PostMapping(value = "/associationDebitCard")
	public Mono<DebitCard> associationDebitCard(@PathVariable("debitCardNumber") String debitCardNumber,
												@PathVariable("numberAccount") String numberAccount,
												@PathVariable("dni") String dni){

		DebitCard dataDebit = new DebitCard();
		Mono.just(dataDebit).doOnNext(t -> {
					t.setDebitCardNumber(debitCardNumber);
					t.setAccountNumber(numberAccount);
					t.setCreationDate( new Date()) ;
					t.setModificationDate(new Date());
					t.setStatus("active");
					t.setDni(dni);
				}).onErrorReturn(dataDebit).onErrorResume(e -> Mono.just(dataDebit))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<DebitCard> debitCardMono = debitCardService.saveDebitCard(dataDebit,false);
		return debitCardMono;
	}

	//Update main account of debit card
	//@CircuitBreaker(name = "passive", fallbackMethod = "fallBackGetSaving")
	@PutMapping("/updateSavingAccount/{accountNumber}")
	public Mono<DebitCard> updateSavingAccount(@PathVariable("accountNumber") String accountNumber){

		DebitCard dataDebitCard = new DebitCard();
		Mono.just(dataDebitCard).doOnNext(t -> {
					t.setAccountNumber(accountNumber);
					t.setModificationDate(new Date());
			t.setModificationDate(new Date());
		}).onErrorReturn(dataDebitCard).onErrorResume(e -> Mono.just(dataDebitCard))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<DebitCard> updatePassive = debitCardService.updateMainDebitCard(dataDebitCard);
		return updatePassive;
	}

	private Mono<DebitCard> fallBackGetSaving(Exception e){
		DebitCard debitCard = new DebitCard();
		Mono<DebitCard> staffMono= Mono.just(debitCard);
		return staffMono;
	}
}
