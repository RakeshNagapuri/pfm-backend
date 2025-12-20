package com.pfm.backend.service;

import java.time.YearMonth;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pfm.backend.dto.BudgetRequestDto;
import com.pfm.backend.dto.response.BudgetResponseDto;
import com.pfm.backend.dto.response.CategoryResponseDto;
import com.pfm.backend.model.Budget;
import com.pfm.backend.model.Category;
import com.pfm.backend.model.User;
import com.pfm.backend.repository.BudgetRepository;
import com.pfm.backend.repository.CategoryRepository;
import com.pfm.backend.repository.UserRepository;
import com.pfm.backend.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetService {
	private final BudgetRepository budgetRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	
	public ResponseEntity<?>createBudget(BudgetRequestDto aBudgetRequestDto,String aEmail){
		User user = userRepository.findByEmail(aEmail).orElse(null);
        if (user == null) {
            return ResponseUtil.build(HttpStatus.UNAUTHORIZED, "User not found");
        }
        
        YearMonth month = YearMonth.parse(aBudgetRequestDto.getMonth());
        Category category = null;
        if(aBudgetRequestDto.getCategoryId()!=null) {
        	category=categoryRepository.findById(aBudgetRequestDto.getCategoryId()).orElse(null);
        	if(category==null || !category.getUser().getId().equals(user.getId())) {
        		return ResponseUtil.build(HttpStatus.BAD_REQUEST, "Invalid Category");
        	}
        }
        
        boolean exists = budgetRepository.findByUserAndMonthAndCategory(user, month, category).isPresent();
        if(exists) {
        	return ResponseUtil.build(HttpStatus.CONFLICT, "Budget already exists for this month");
        }
        
        Budget budget = Budget.builder()
        		  .month(month)
        		  .amount(aBudgetRequestDto.getAmount())
        		  .user(user)
        		  .category(category)
        		  .build();
        budgetRepository.save(budget);
        
        BudgetResponseDto response = new BudgetResponseDto(
                budget.getId(),
                budget.getMonth().toString(),
                budget.getAmount(),
                category == null ? null :
                        new CategoryResponseDto(
                                category.getId(),
                                category.getName(),
                                category.getType()
                        )
        );
        return ResponseUtil.build(
                HttpStatus.CREATED,
                "Budget created successfully",
                response
        );
        
	}
	
	public ResponseEntity<?>getBudgets(String aEmail,String monthStr){
		
		User user = userRepository.findByEmail(aEmail).orElse(null);
	    if (user == null) {
	        return ResponseUtil.build(
	                HttpStatus.UNAUTHORIZED,
	                "User not found"
	        );
	    }
	    List<Budget> budgets;

	    if (monthStr != null && !monthStr.isBlank()) {
	        YearMonth month = YearMonth.parse(monthStr);
	        budgets = budgetRepository.findByUserAndMonth(user, month);
	    } else {
	        budgets = budgetRepository.findByUser(user);
	    }
	    
	    List<BudgetResponseDto> response = budgets.stream()
	            .map(b -> new BudgetResponseDto(
	                    b.getId(),
	                    b.getMonth().toString(),
	                    b.getAmount(),
	                    b.getCategory() == null ? null :
	                            new CategoryResponseDto(
	                                    b.getCategory().getId(),
	                                    b.getCategory().getName(),
	                                    b.getCategory().getType()
	                            )
	            ))
	            .toList();
	    
	    return ResponseUtil.build(
	            HttpStatus.OK,
	            "Budgets fetched successfully",
	            response
	    );
	    
	    
	}
}
