package com.pfm.backend.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pfm.backend.model.Transaction;
import com.pfm.backend.model.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	
	Optional<Transaction> findByIdAndUser(Long aId,User aUser);	
	
	Page<Transaction> findByUserAndTransactionDateBetweenAndType(User aUser, LocalDate aFromDate, LocalDate aToDate,
			String aType, Pageable aPageable);
	
	Page<Transaction> findByUser(User aUser, Pageable aPageable);
	
	@Query("""
			SELECT COALESCE(SUM(t.amount),0) FROM Transaction t
			where t.user = :user
			AND t.type = 'EXPENSE'
			AND t.transactionDate BETWEEN :start AND :end
			""")
	BigDecimal sumExpenseForPeriod(@Param("user")User user,@Param("start")LocalDate aStart,@Param("end")LocalDate aEnd);
	
	
	@Query("""
			SELECT t.category.id,COALESCE(SUM(t.amount),0) FROM Transaction t 
			where t.user = :user
			AND t.type ='EXPENSE'
			AND t.transactionDate BETWEEN :start AND :end
			GROUP BY t.category.id
			""")
	List<Object[]>sumExpensesByCategory(@Param("user")User aUser,@Param("start")LocalDate aStart,@Param("end")LocalDate aEnd);
	
	@Query("""
		    SELECT COALESCE(SUM(t.amount), 0)
		    FROM Transaction t
		    WHERE t.user = :user
		      AND t.type = :type
		      AND t.transactionDate BETWEEN :start AND :end
		""")
	BigDecimal sumByTypeAndPeriod(
	        @Param("user") User user,
	        @Param("type") String type,
	        @Param("start") LocalDate start,
	        @Param("end") LocalDate end
	);
	
	@Query("""
		    SELECT c.id, c.name, COALESCE(SUM(t.amount), 0)
		    FROM Transaction t
		    JOIN t.category c
		    WHERE t.user = :user
		      AND t.type = 'EXPENSE'
		      AND t.transactionDate BETWEEN :start AND :end
		    GROUP BY c.id, c.name
		""")
	List<Object[]> getCategoryWiseExpenseSummary(
		        @Param("user") User user,
		        @Param("start") LocalDate start,
		        @Param("end") LocalDate end
		);
}
