package com.transitops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transitops.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	List<Expense> findAllByOrderByLogDateDescIdDesc();
}
