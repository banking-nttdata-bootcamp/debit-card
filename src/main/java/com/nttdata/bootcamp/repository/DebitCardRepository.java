package com.nttdata.bootcamp.repository;

import com.nttdata.bootcamp.entity.DebitCard;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface DebitCardRepository extends ReactiveCrudRepository<DebitCard, String> {
}
