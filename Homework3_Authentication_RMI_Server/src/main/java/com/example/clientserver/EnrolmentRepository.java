package com.example.clientserver;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnrolmentRepository extends JpaRepository<Enrolment, Long> {
	@Query("SELECT e FROM Enrolment e WHERE e.studentNumber = :studentNumber AND e.courseNumber = :courseNumber")
	Optional<Enrolment> findByStudentNumberAndCourseNumber(@Param("studentNumber") String studentNumber, @Param("courseNumber") String courseNumber);

	List<Enrolment> findAllByStudentNumber(String studentNumber);
}
