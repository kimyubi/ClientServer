package com.example.clientserver;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;

@Getter
public class CreateCourseDto implements Serializable {
	private static final long serialVersionUID = 2L;
	private String courseNumber;

	private String professor;

	private String name;

	private List<String> prerequisiteSubjectList;

	public CreateCourseDto(String courseNumber, String professor, String name, List<String> prerequisiteSubjectList) {
		this.courseNumber = courseNumber;
		this.professor = professor;
		this.name = name;
		this.prerequisiteSubjectList = prerequisiteSubjectList;
	}
}
