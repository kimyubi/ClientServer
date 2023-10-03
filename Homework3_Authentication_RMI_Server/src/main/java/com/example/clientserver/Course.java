package com.example.clientserver;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String courseNumber;

	private String professor;

	private String name;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> prerequisiteSubjectList = new LinkedList<>();

	@Builder
	public Course(String courseNumber, String professor, String name, List<String> prerequisiteSubjectList) {
		this.courseNumber = courseNumber;
		this.professor = professor;
		this.name = name;
		this.prerequisiteSubjectList = prerequisiteSubjectList;
	}
}
