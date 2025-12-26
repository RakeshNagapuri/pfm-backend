package com.pfm.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pfm.backend.dto.BudgetRequestDto;
import com.pfm.backend.dto.BudgetUpdateRequestDto;
import com.pfm.backend.dto.response.BudgetResponseDto;
import com.pfm.backend.dto.response.BudgetSummaryResponseDto;
import com.pfm.backend.dto.response.CategoryBudgetSummaryResponseDto;
import com.pfm.backend.dto.response.CategoryResponseDto;
import com.pfm.backend.model.Budget;
import com.pfm.backend.model.Category;
import com.pfm.backend.model.User;
import com.pfm.backend.repository.BudgetRepository;
import com.pfm.backend.repository.CategoryRepository;
import com.pfm.backend.repository.TransactionRepository;
import com.pfm.backend.repository.UserRepository;
import com.pfm.backend.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetService {
	private final BudgetRepository budgetRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	private final TransactionRepository transactionRepository;
	
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
	
	public ResponseEntity<?> updateBudget(Long aBudgetId,BudgetUpdateRequestDto aDto,String aEmail){
		User user = userRepository.findByEmail(aEmail).orElse(null);
	    if (user == null) {
	        return ResponseUtil.build(
	                HttpStatus.UNAUTHORIZED,
	                "User not found"
	        );
	    }
	    Budget budget = budgetRepository.findById(aBudgetId).orElse(null);
	    if (budget == null || !budget.getUser().getId().equals(user.getId())) {
	        return ResponseUtil.build(
	                HttpStatus.NOT_FOUND,
	                "Budget not found"
	        );
	    }
	    
	    budget.setAmount(aDto.getAmount());;
	    budgetRepository.save(budget);
	    
	    BudgetResponseDto response = new BudgetResponseDto(
	            budget.getId(),
	            budget.getMonth().toString(),
	            budget.getAmount(),
	            budget.getCategory() == null ? null :
	                    new CategoryResponseDto(
	                            budget.getCategory().getId(),
	                            budget.getCategory().getName(),
	                            budget.getCategory().getType()
	                    )
	    );
	    return ResponseUtil.build(
	            HttpStatus.OK,
	            "Budget updated successfully",
	            response
	    );
	}
	
	public ResponseEntity<?>getBudgetSummary(String aEmail,String aMonthStr){
		User user = userRepository.findByEmail(aEmail).orElse(null);
	    if (user == null) {
	        return ResponseUtil.build(
	                HttpStatus.UNAUTHORIZED,
	                "User not found"
	        );
	    }
	    YearMonth month = YearMonth.parse(aMonthStr);
	    LocalDate start = month.atDay(1);
	    LocalDate end = month.atEndOfMonth();
	    
	    Budget budget = budgetRepository.findByUserAndMonthAndCategory(user, month, null).orElse(null);
	    if(budget==null) {
	    	return ResponseUtil.build(HttpStatus.NOT_FOUND,
	    			"No overall budget found for this month");
	    }
	    BigDecimal spent = transactionRepository.sumExpenseForPeriod(user, start, end);
	    BigDecimal remaining = budget.getAmount().subtract(spent);
	    BudgetSummaryResponseDto response =
	            new BudgetSummaryResponseDto(
	                    month.toString(),
	                    budget.getAmount(),
	                    spent,
	                    remaining,
	                    remaining.compareTo(BigDecimal.ZERO) < 0
	            );
	    return ResponseUtil.build(HttpStatus.OK, "Budget summary fetched successfully",response);	    
	}
	
	public ResponseEntity<?>getCategoryBudgetSummary(String aEmail,String aMonthStr){
		User user = userRepository.findByEmail(aEmail).orElse(null);
	    if (user == null) {
	        return ResponseUtil.build(
	                HttpStatus.UNAUTHORIZED,
	                "User not found"
	        );
	    }
	    
	    YearMonth month = YearMonth.parse(aMonthStr);
	    LocalDate start = month.atDay(1);
	    LocalDate end = month.atEndOfMonth();
	    // 1. Fetch category budgets
	    List<Budget> categoryBudgets = 
	    		budgetRepository.findByUserAndMonthAndCategoryIsNotNull(user, month);
	    
	    // 2. Fetch spent amounts grouped by Category
	    Map<Long,BigDecimal> spentMap = new HashMap<>();
	    for(Object[] row : transactionRepository.sumExpensesByCategory(user, start, end)) {
	    	spentMap.put((Long)row[0], (BigDecimal)row[1]);
	    }
	    // 3. Build response
	    List<CategoryBudgetSummaryResponseDto> response = categoryBudgets.stream()
	    		.map(budget->{
	    			BigDecimal spent = spentMap.getOrDefault(budget.getCategory().getId(), BigDecimal.ZERO);
	    			BigDecimal remaining = budget.getAmount().subtract(spent);
	    			return new CategoryBudgetSummaryResponseDto(
	    					new CategoryResponseDto(budget.getCategory().getId(), budget.getCategory().getName(), budget.getCategory().getType()),
	    					budget.getAmount(),
	    					spent,
	    					remaining,
	    					remaining.compareTo(BigDecimal.ZERO)<0);
	    		}).toList();
	    return ResponseUtil.build(HttpStatus.OK, "Category budget summary fetched successfully",response);
	    
	}
}
