package com.pfm.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pfm.backend.dto.common.ApiResponse;
import com.pfm.backend.dto.response.OverBudgetSummaryResponseDto;
import com.pfm.backend.model.Budget;
import com.pfm.backend.model.User;
import com.pfm.backend.repository.BudgetRepository;
import com.pfm.backend.repository.TransactionRepository;
import com.pfm.backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {
	@Mock
	private BudgetRepository budgetRepository;
	
	@Mock
	private TransactionRepository transactionRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private BudgetService budgetService;
	
	private User user;
	
	@BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
    }
	
	@Test
	public void shouldDetectOverallOverBudget() {
		YearMonth month = YearMonth.of(2025,1);
		LocalDate start = month.atDay(1);
		LocalDate end = month.atEndOfMonth();
		
		// mock budget and repositories output
		Budget overallBudget = new Budget();
		overallBudget.setAmount(BigDecimal.valueOf(10000));
		
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		
		when(budgetRepository.findByUserAndMonthAndCategory(user, month, null)).thenReturn(Optional.of(overallBudget));
		
		when(transactionRepository.sumExpenseForPeriod(user, start, end)).thenReturn(BigDecimal.valueOf(12000));
		
		var response = budgetService.getOverBudgetSummary(user.getEmail(), "2025-01");
		
		assertEquals(200, response.getStatusCode().value());
		
		ApiResponse apiResponse = (ApiResponse) response.getBody();
		OverBudgetSummaryResponseDto data = (OverBudgetSummaryResponseDto) apiResponse.getData();
		
		assertTrue(data.isOverallOverBudget());
		assertEquals(BigDecimal.valueOf(2000), data.getOverallExceededAmount());
	}
	
	@Test
	public void shouldNotBeOverBudgetWhenSpentWithinLimit() {
		YearMonth month = YearMonth.of(2025,1);
		LocalDate start = month.atDay(1);
		LocalDate end = month.atEndOfMonth();
		
		// mock budget and repositories output
		Budget overallBudget = new Budget();
		overallBudget.setAmount(BigDecimal.valueOf(15000));
		
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		
		when(budgetRepository.findByUserAndMonthAndCategory(user, month, null)).thenReturn(Optional.of(overallBudget));
		
		when(transactionRepository.sumExpenseForPeriod(user, start, end)).thenReturn(BigDecimal.valueOf(12000));
		
		var response = budgetService.getOverBudgetSummary(user.getEmail(), "2025-01");
		
		assertEquals(200, response.getStatusCode().value());	
		
		ApiResponse apiResponse = (ApiResponse) response.getBody();
		OverBudgetSummaryResponseDto data = (OverBudgetSummaryResponseDto) apiResponse.getData();
		
		assertFalse(data.isOverallOverBudget());
		assertEquals(BigDecimal.ZERO, data.getOverallExceededAmount());
	}
}
