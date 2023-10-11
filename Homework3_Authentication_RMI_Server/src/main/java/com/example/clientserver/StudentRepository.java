package com.example.clientserver;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
	Optional<Student> findByStudentNumber(String studentNumber);
}
