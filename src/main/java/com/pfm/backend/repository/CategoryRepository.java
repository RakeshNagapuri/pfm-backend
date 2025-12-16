package com.pfm.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfm.backend.model.Category;
import com.pfm.backend.model.User;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	List<Category>findByUser(User user);

	Optional<Category> findByIdAndUser(Long id, User user);
}
