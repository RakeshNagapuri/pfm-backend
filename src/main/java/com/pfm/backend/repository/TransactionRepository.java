package com.pfm.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfm.backend.model.Transaction;
import com.pfm.backend.model.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	
	List<Transaction>findByUser(User aUser);
	
	Optional<Transaction> findByIdAndUser(Long aId,User aUser);

}
