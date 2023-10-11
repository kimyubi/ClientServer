package com.example.clientserver;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;

@Getter
public class CreateStudentDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String studentNumber;
	private String name;
	private String department;
	private List<String> completedCoursesList;

	public CreateStudentDto(String studentNumber, String name, String department, List<String> completedCoursesList) {
		this.studentNumber = studentNumber;
		this.name = name;
		this.department = department;
		this.completedCoursesList = completedCoursesList;
	}
}
