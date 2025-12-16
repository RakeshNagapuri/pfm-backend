package com.pfm.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pfm.backend.dto.CategoryRequestDto;
import com.pfm.backend.model.Category;
import com.pfm.backend.model.User;
import com.pfm.backend.repository.CategoryRepository;
import com.pfm.backend.repository.UserRepository;
import com.pfm.backend.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository categoryRepo;
	private final UserRepository userRepo;
	
	public ResponseEntity<?>createCategory(CategoryRequestDto aRequestDto,String aEmail){
		User user = userRepo.findByEmail(aEmail).orElse(null);
		
		if (user == null) {
	        return ResponseUtil.build(
	                HttpStatus.UNAUTHORIZED,
	                "User not found"
	        );
	    }
		
		Category category = Category.builder().name(aRequestDto.getName())
			.type(aRequestDto.getType())
			.user(user)
			.build();
		
		categoryRepo.save(category);

        return ResponseUtil.build(
                HttpStatus.CREATED,
                "Category created successfully"
        );
	}
	
	public ResponseEntity<?> getCategories(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        
        if (user == null) {
            return ResponseUtil.build(
                    HttpStatus.UNAUTHORIZED,
                    "User not found"
            );
        }

        List<Category> categories = categoryRepo.findByUser(user);

        return ResponseUtil.build(
                HttpStatus.OK,
                "Categories fetched successfully",
                categories
        );
    }
	
}
