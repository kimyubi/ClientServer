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

	// 학생별 수강 과목 리스트 : 학번을 넣으면 수강과목 정보가 나옴
	// List<Course> getRegisteredCourses(String studentNumber) throws IOException;

	// 학생 별 수강을 완료한 과목 리스트
	// List<Course> getCompletedCoursesList(String studentNumber) throws IOException;

	// 수강 신청 : 학번과 수강과목 ID를 넣으면 수강 신청이 이루어짐. 중복 수강 신청 및 일정 충돌 문제 체크 후 수강 신청이 되어야 함
}
