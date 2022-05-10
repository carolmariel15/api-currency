package com.nttdata.api.currency.repository;

import com.nttdata.api.currency.document.Currency;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrecyRepository extends ReactiveMongoRepository<Currency, Integer> {
}
