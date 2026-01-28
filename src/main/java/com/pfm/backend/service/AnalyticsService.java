package com.pfm.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pfm.backend.dto.response.CategoryBudgetVsActualDto;
import com.pfm.backend.dto.response.CategoryExpenseSummaryDto;
import com.pfm.backend.dto.response.MonthlySummaryResponseDto;
import com.pfm.backend.model.Budget;
import com.pfm.backend.model.User;
import com.pfm.backend.repository.BudgetRepository;
import com.pfm.backend.repository.TransactionRepository;
import com.pfm.backend.repository.UserRepository;
import com.pfm.backend.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final BudgetRepository budgetRepository;
	private final TransactionRepository transactionRepository;
	private final UserRepository userRepository;

	
	
	public ResponseEntity<?>getMonthlySummary(String aEmail,String aMonthStr){
		User user = userRepository.findByEmail(aEmail).orElse(null);
		if(user==null) {
			return ResponseUtil.build(HttpStatus.UNAUTHORIZED, "User not found");
		}
		YearMonth month = YearMonth.parse(aMonthStr);
		LocalDate start = month.atDay(1);
		LocalDate end = month.atEndOfMonth();
		
		BigDecimal income = transactionRepository.sumByTypeAndPeriod(user, "INCOME", start, end);
		
		BigDecimal expenses = transactionRepository.sumByTypeAndPeriod(user, "EXPENSE", start, end);
		
		BigDecimal savings = income.subtract(expenses);
		
		return ResponseUtil.build(HttpStatus.OK, "Monthly summary fetched successfully",new MonthlySummaryResponseDto(aMonthStr,income,expenses,savings));
		
	}
	
	public ResponseEntity<?> getCategoryWiseExpenseSummary(
	        String email,
	        String monthStr
	) {

	    User user = userRepository.findByEmail(email).orElse(null);
	    if (user == null) {
	        return ResponseUtil.build(
	                HttpStatus.UNAUTHORIZED,
	                "User not found"
	        );
	    }
	    YearMonth month = YearMonth.parse(monthStr);
	    LocalDate start = month.atDay(1);
	    LocalDate end = month.atEndOfMonth();
	    
	    List<Object[]> results = transactionRepository.getCategoryWiseExpenseSummary(user,start,end);
	    
	    List<CategoryExpenseSummaryDto>response = 
	    		results.stream().map(r->new CategoryExpenseSummaryDto((Long)r[0], (String)r[1], (BigDecimal)r[2])).toList();
	    
	    return ResponseUtil.build(
	            HttpStatus.OK,
	            "Category-wise expense summary fetched successfully",
	            response
	    );
	}
	
	public ResponseEntity<?> getCategoryBudgetVsActual(String aEmail,String aMonthStr){
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
	    List<Budget> categoryBudgets = budgetRepository.findByUserAndMonthAndCategoryIsNotNull(user, month);
	    if(categoryBudgets.isEmpty()) {
	    	return ResponseUtil.build(HttpStatus.OK, "No category budgets found for this month");
	    }
	    
	    //2. Fetch actual expenses per category
	    List<Object[]> expenses = transactionRepository.sumExpensesByCategory(user, start, end);
	    
	    Map<Long, BigDecimal> expenseMap = expenses.stream().collect(Collectors.toMap(e->(Long)e[0], e->(BigDecimal)e[1]));
	    
	    List<CategoryBudgetVsActualDto> response = categoryBudgets.stream().map(budget->{
	    	Long categoryId = budget.getCategory().getId();
	    	BigDecimal expense = expenseMap.getOrDefault(categoryId, BigDecimal.ZERO);
	    	
	    	BigDecimal remaining = budget.getAmount().subtract(expense);
	    	return new CategoryBudgetVsActualDto(
	    			categoryId,
	    			budget.getCategory().getName(),
	    			budget.getAmount(),
	    			expense,
	    			remaining,
	    			remaining.compareTo(BigDecimal.ZERO)<0);
	    	
	    }).toList();
	    return ResponseUtil.build(HttpStatus.OK, "Budget vs actual fetched successfully",response);
	    
	}
}
