package com.pfm.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.pfm.backend.dto.common.ApiResponse;
import com.pfm.backend.dto.response.CategoryOverBudgetDto;
import com.pfm.backend.dto.response.OverBudgetSummaryResponseDto;
import com.pfm.backend.model.Budget;
import com.pfm.backend.model.Category;
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
	
	/**
	 * Overall budget over-limit case.
	 *
	 * Month: 2025-01
	 * Overall Budget: 10,000
	 * Total Expenses: 12,000
	 *
	 * Expected:
	 * - overallOverBudget = true
	 * - overallExceededAmount = 2,000
	 */
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
	/**
	 * Overall budget within limit case.
	 *
	 * Month: 2025-01
	 * Overall Budget: 15,000
	 * Total Expenses: 12,000
	 *
	 * Expected:
	 * - overallOverBudget = false
	 * - overallExceededAmount = 0
	 */
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
	
	/**
	 * Category-wise over-budget case.
	 *
	 * Month: 2025-01
	 * Category: Food (ID: 1)
	 * Category Budget: 8,000
	 * Category Expenses: 9,200
	 *
	 * Expected:
	 * - Category "Food" present in over-budget list
	 * - exceededAmount = 1,200
	 */
	@Test
	public void shouldDetectCategoryWiseOverBudget() {
		Category food = new Category();
		food.setId(1L);
		food.setName("Food");
		food.setType("EXPENSE");
		
		Budget foodBudget = new Budget();
		foodBudget.setAmount(BigDecimal.valueOf(8000));
		foodBudget.setCategory(food);
		
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		when(budgetRepository.findByUserAndMonthAndCategoryIsNotNull(eq(user), eq(YearMonth.of(2025, 1))))
				.thenReturn(List.of(foodBudget));
		List<Object[]> result = new ArrayList<>();
		result.add(new Object[] { 1L, BigDecimal.valueOf(9200) });

		when(transactionRepository.sumExpensesByCategory(
		        eq(user),
		        any(LocalDate.class),
		        any(LocalDate.class)))
		    .thenReturn(result);

		
		 ResponseEntity<?> response =
		            budgetService.getOverBudgetSummary(
		                    user.getEmail(), "2025-01");

		    ApiResponse apiResponse = (ApiResponse) response.getBody();
		    OverBudgetSummaryResponseDto data =
		            (OverBudgetSummaryResponseDto) apiResponse.getData();

		    assertEquals(1, data.getCategoryOverBudgets().size());

		    CategoryOverBudgetDto violation =
		            data.getCategoryOverBudgets().get(0);

		    assertEquals("Food", violation.getCategory().getName());
		    assertEquals(
		            BigDecimal.valueOf(1200),
		            violation.getExceededAmount()
		    );
	}
	/**
	 * No overall budget present.
	 *
	 * Month: 2025-01
	 * Overall Budget: null
	 * Category Budgets: present
	 *
	 * Expected:
	 * - overallOverBudget = false
	 * - overallExceededAmount = 0
	 * - No exception thrown
	 */
	@Test
	public void shouldHandleMissingOverallBudgetGracefully() {
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		when(budgetRepository.findByUserAndMonthAndCategory(eq(user),eq(YearMonth.of(2025, 1)) , isNull())).thenReturn(Optional.empty());
		when(transactionRepository.sumExpenseForPeriod(any(), any(), any())).thenReturn(BigDecimal.valueOf(5000));
		
		ResponseEntity<?>response=budgetService.getOverBudgetSummary(user.getEmail(), "2025-01");
		ApiResponse apiResponse = (ApiResponse)response.getBody();
		OverBudgetSummaryResponseDto data = (OverBudgetSummaryResponseDto)apiResponse.getData();
		
		assertFalse(data.isOverallOverBudget());
		assertEquals(BigDecimal.ZERO, data.getOverallExceededAmount());
	}
	/**
	 * No category exceeds its budget.
	 *
	 * Month: 2025-01
	 * Category Budgets: within limits
	 *
	 * Expected:
	 * - categoryOverBudgets list is empty
	 */
	@Test
	public void shouldReturnEmptyListWhenNoCategoryIsOverBudget() {
		Category food = new Category();
		food.setId(1L);
		food.setName("Food");
		food.setType("EXPENSE");
		
		Budget foodBudget = new Budget();
		foodBudget.setAmount(BigDecimal.valueOf(10000));
		foodBudget.setCategory(food);
		
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		when(budgetRepository.findByUserAndMonthAndCategoryIsNotNull(eq(user),eq(YearMonth.of(2025, 1)))).thenReturn(List.of(foodBudget));
		List<Object[]>spent = new ArrayList<>();
		spent.add(new Object[] {1L,BigDecimal.valueOf(6000)});
		
		when(transactionRepository.sumExpensesByCategory(any(), any(), any())).thenReturn(spent);
		ResponseEntity<?>response = budgetService.getOverBudgetSummary(user.getEmail(), "2025-01");
		ApiResponse apiResponse = (ApiResponse)response.getBody();
		OverBudgetSummaryResponseDto data = (OverBudgetSummaryResponseDto)apiResponse.getData();
		assertTrue(data.getCategoryOverBudgets().isEmpty());		
	}
}
