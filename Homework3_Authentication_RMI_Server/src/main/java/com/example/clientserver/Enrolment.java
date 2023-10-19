package com.example.clientserver;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Enrolment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String studentNumber;

	private String courseNumber;

	@Builder
	public Enrolment(String studentNumber, String courseNumber) {
		this.studentNumber = studentNumber;
		this.courseNumber = courseNumber;
	}
}
