package com.example.clientserver;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIInterface extends Remote {
	String signUp(String id, String pwd, String name) throws RemoteException, IOException;
	String login(String id, String pwd) throws RemoteException, IOException;

	void initializationStudent(String fileName) throws RemoteException, IOException;

	void initializationCourse(String fileName) throws RemoteException, IOException;

	// 학생 리스트
	List<String> getAllStudents() throws RemoteException, IOException;

	// 강의 목록
	List<String> getAllCourses() throws RemoteException, IOException;

	String addStudent(CreateStudentDto dto) throws RemoteException, IOException;

	String deleteStudent(String studentNumber) throws RemoteException, IOException;

	String addCourse(CreateCourseDto dto) throws RemoteException, IOException;
	String deleteCourse(String courseNumber) throws RemoteException, IOException;
	String enrolment(String studentNumber, String courseNumber) throws RemoteException, IOException;
}
