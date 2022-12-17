package com.nttdata.bootcamp.controller;

import com.nttdata.bootcamp.entity.DebitCard;
import com.nttdata.bootcamp.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.nttdata.bootcamp.service.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Date;
import com.nttdata.bootcamp.entity.dto.DebitCardDto;

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
	//@CircuitBreaker(name = "passive", fallbackMethod = "fallBackGetDebitCard")
	@GetMapping("/findMainAccountsByDebitCard/{accountNumber}")
	public Mono<DebitCard> findMainAccountsByDebitCard(@PathVariable("debitCardNumber") String debitCardNumber) {
		LOGGER.info("Searching main Account Products by debit card: " + debitCardNumber);
		return debitCardService.findMainAccountsByDebitCard(debitCardNumber);
	}

	@GetMapping("/findByAccountNumber/{accountNumber}")
	public Mono<DebitCard> findByAccountNumber(@PathVariable("debitCardNumber") String debitCardNumber) {
		LOGGER.info("Searching main Account Products by debit card: " + debitCardNumber);
		return debitCardService.findMainAccountsByDebitCard(debitCardNumber);
	}


	//Save Debit Card
	//@CircuitBreaker(name = "passive", fallbackMethod = "fallBackGetDebitCard")
	@PostMapping(value = "/saveDebitCard")
	public Mono<DebitCard> saveDebitCard(@RequestBody DebitCardDto debitCard){

		DebitCard dataDebit = new DebitCard();
		Mono.just(dataDebit).doOnNext(t -> {
			t.setDni(debitCard.getDni());
			t.setTypeCustomer(debitCard.getTypeCustomer());
			t.setAccountNumber(debitCard.getAccountNumber());
			t.setDebitCardNumber(debitCard.getDebitCardNumber());
			t.setStatus(Constant.DEBITCARD_ACTIVE);
			t.setMainAccount(false);
			t.setCreationDate(new Date());
			t.setModificationDate(new Date());
		}).onErrorReturn(dataDebit).onErrorResume(e -> Mono.just(dataDebit))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<DebitCard> passiveMono = debitCardService.saveDebitCard(dataDebit,true);
		return passiveMono;
	}
	@PostMapping(value = "/associationDebitCard")
	public Mono<DebitCard> associationDebitCard(@PathVariable("debitCardNumber") String debitCardNumber,
												@PathVariable("numberAccount") String numberAccount){
		DebitCard dataDebit = new DebitCard();

		Mono.just(dataDebit).doOnNext(t -> {
					t.setDebitCardNumber(debitCardNumber);
					t.setAccountNumber(numberAccount);
					t.setCreationDate( new Date()) ;
					t.setModificationDate(new Date());
					t.setStatus(Constant.DEBITCARD_ACTIVE);
					t.setMainAccount(true);
				}).onErrorReturn(dataDebit).onErrorResume(e -> Mono.just(dataDebit))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<DebitCard> debitCardMono = debitCardService.updateMainDebitCard(dataDebit);
		return debitCardMono;
	}
	@PostMapping(value = "/deleteAssociationDebitCard")
	public Mono<DebitCard> deleteAssociationDebitCard(@PathVariable("debitCardNumber") String debitCardNumber,
												@PathVariable("numberAccount") String numberAccount){

		DebitCard dataDebit = new DebitCard();
		Mono.just(dataDebit).doOnNext(t -> {
					t.setDebitCardNumber(debitCardNumber);
					t.setAccountNumber(numberAccount);
					t.setCreationDate( new Date()) ;
					t.setModificationDate(new Date());
					t.setStatus(Constant.DEBITCARD_ACTIVE);
					t.setMainAccount(false);
				}).onErrorReturn(dataDebit).onErrorResume(e -> Mono.just(dataDebit))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<DebitCard> debitCardMono = debitCardService.updateMainDebitCard(dataDebit);
		return debitCardMono;
	}



	private Mono<DebitCard> fallBackGetDebitCard(Exception e){
		DebitCard debitCard = new DebitCard();
		Mono<DebitCard> staffMono= Mono.just(debitCard);
		return staffMono;
	}
}
