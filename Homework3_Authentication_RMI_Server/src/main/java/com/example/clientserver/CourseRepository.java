package com.example.clientserver;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
	Optional<Course> findByCourseNumber(String courseNumber);
}
