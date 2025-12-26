package com.pfm.backend.repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfm.backend.model.Budget;
import com.pfm.backend.model.Category;
import com.pfm.backend.model.User;

public interface BudgetRepository extends JpaRepository<Budget, Long>{
	Optional<Budget>findByUserAndMonthAndCategory(User aUser,YearMonth aMonth,Category aCategory);

	List<Budget> findByUserAndMonth(User aUser, YearMonth aMonth);

	List<Budget> findByUser(User aUser);
	
	List<Budget> findByUserAndMonthAndCategoryIsNotNull(User user, YearMonth month);
	
}
