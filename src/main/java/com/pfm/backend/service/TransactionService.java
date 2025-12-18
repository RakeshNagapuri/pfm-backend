package com.pfm.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pfm.backend.dto.TransactionRequestDto;
import com.pfm.backend.model.Category;
import com.pfm.backend.model.Transaction;
import com.pfm.backend.model.User;
import com.pfm.backend.repository.CategoryRepository;
import com.pfm.backend.repository.TransactionRepository;
import com.pfm.backend.repository.UserRepository;
import com.pfm.backend.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	
	public ResponseEntity<?>createTransaction(TransactionRequestDto aTransactionRequestDto,String aEmail){
		User user = userRepository.findByEmail(aEmail).orElse(null);
		if(user==null) {
			return ResponseUtil.build(
                    HttpStatus.UNAUTHORIZED,
                    "User not found"
            );
		}
		Category category = categoryRepository
                .findById(aTransactionRequestDto.getCategoryId())
                .orElse(null);

        if (category == null || !category.getUser().getId().equals(user.getId())) {
            return ResponseUtil.build(
                    HttpStatus.BAD_REQUEST,
                    "Invalid category"
            );
        }
        
        Transaction transaction = Transaction.builder().amount(aTransactionRequestDto.getAmount())
        .type(aTransactionRequestDto.getType())
        .transactionDate(aTransactionRequestDto.getTransactionDate())
        .description(aTransactionRequestDto.getDescription())
        .user(user)
        .category(category)
        .build();
        
        transactionRepository.save(transaction);
        
        return ResponseUtil.build(HttpStatus.CREATED, "Transaction created successfully");
	}

	public ResponseEntity<?> getTransactions(String aEmail) {
		User user = userRepository.findByEmail(aEmail).orElse(null);
		if(user==null) {
			return ResponseUtil.build(
                    HttpStatus.UNAUTHORIZED,
                    "User not found"
            );
		}
		
		return ResponseUtil.build(HttpStatus.OK,"Transactions fetched successfully" ,transactionRepository.findByUser(user));
	}

	public ResponseEntity<?> updateTransaction(Long aId, TransactionRequestDto aRequestDto, String aEmail) {
		User user = userRepository.findByEmail(aEmail).orElse(null);
		if(user==null) {
			return ResponseUtil.build(
                    HttpStatus.UNAUTHORIZED,
                    "User not found"
            );
		}
		Transaction transaction = transactionRepository
	            .findByIdAndUser(aId, user)
	            .orElse(null);

	    if (transaction == null) {
	        return ResponseUtil.build(
	                HttpStatus.NOT_FOUND,
	                "Transaction not found"
	        );
	    }
	    
	    Category category = categoryRepository
	            .findById(aRequestDto.getCategoryId())
	            .orElse(null);

	    if (category == null || !category.getUser().getId().equals(user.getId())) {
	        return ResponseUtil.build(
	                HttpStatus.BAD_REQUEST,
	                "Invalid category"
	        );
	    }
		
	    transaction.setAmount(aRequestDto.getAmount());
	    transaction.setType(aRequestDto.getType());
	    transaction.setTransactionDate(aRequestDto.getTransactionDate());
	    transaction.setDescription(aRequestDto.getDescription());
	    transaction.setCategory(category);

	    transactionRepository.save(transaction);

	    return ResponseUtil.build(
	            HttpStatus.OK,
	            "Transaction updated successfully"
	    );
	}

	public ResponseEntity<?> deleteTransaction(Long aId, String aEmail) {
		User user = userRepository.findByEmail(aEmail).orElse(null);
		if(user==null) {
			return ResponseUtil.build(
                    HttpStatus.UNAUTHORIZED,
                    "User not found"
            );
		}
		Transaction transaction = transactionRepository
	            .findByIdAndUser(aId, user)
	            .orElse(null);

	    if (transaction == null) {
	        return ResponseUtil.build(
	                HttpStatus.NOT_FOUND,
	                "Transaction not found"
	        );
	    }
	    
	    transactionRepository.delete(transaction);
	    return ResponseUtil.build(
	            HttpStatus.OK,
	            "Transaction deleted successfully"
	    );
	}
}
