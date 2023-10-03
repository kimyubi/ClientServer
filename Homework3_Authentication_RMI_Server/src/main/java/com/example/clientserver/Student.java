package com.example.clientserver;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Fetch;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String studentNumber;

	private String name;

	private String department;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> completedCoursesList = new LinkedList<>();

	@Builder
	public Student(String studentNumber, String name, String department, List<String> completedCoursesList) {
		this.studentNumber = studentNumber;
		this.name = name;
		this.department = department;
		this.completedCoursesList = completedCoursesList;
	}
}
