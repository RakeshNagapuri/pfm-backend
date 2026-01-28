package com.pfm.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pfm.backend.dto.response.CategoryExpenseSummaryDto;
import com.pfm.backend.dto.response.MonthlySummaryResponseDto;
import com.pfm.backend.model.User;
import com.pfm.backend.repository.TransactionRepository;
import com.pfm.backend.repository.UserRepository;
import com.pfm.backend.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
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
}
