package com.pfm.backend.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pfm.backend.model.Transaction;
import com.pfm.backend.model.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	
	Optional<Transaction> findByIdAndUser(Long aId,User aUser);	
	
	Page<Transaction> findByUserAndTransactionDateBetweenAndType(User aUser, LocalDate aFromDate, LocalDate aToDate,
			String aType, Pageable aPageable);
	
	Page<Transaction> findByUser(User user, Pageable pageable);

}
