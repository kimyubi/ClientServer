package com.example.clientserver;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Client2 {
	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			RMIInterface rmiInterface = (RMIInterface) Naming.lookup("rmi://localhost/server");
			while(true){
				printMenue();
				int value = scanner.nextInt();
				switch (value){
					case 0:
						System.out.println("================  [프로그램을 종료합니다] ================ ");
						return;
					case 1:
						signUp(scanner, rmiInterface);
						break;
					case 2:
						logIn(scanner, rmiInterface);
						break;
					case 3:
						printStudentList(rmiInterface);
						break;
					case 4:
						printCourseList(rmiInterface);
						break;
					case 5:
						addStudent(scanner,rmiInterface);
						break;
					case 6:
						deleteStudent(scanner,rmiInterface);
						break;
					case 7:
						addCourse(scanner, rmiInterface);
						break;
					case 8:
						deleteCourse(scanner, rmiInterface);
						break;
					case 9:
						enrolment(scanner, rmiInterface);
						break;
					case 10:
						printEnrolmentByStudentNumber(scanner, rmiInterface);
						break;
					default:
						System.out.println("잘못 입력하셨습니다.");
						break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printEnrolmentByStudentNumber(Scanner scanner, RMIInterface rmiInterface) throws
		RemoteException {
		System.out.println("================ [수강 신청 내역 확인] ================");
		System.out.println("학번을 입력하세요.");
		String studentNumber = scanner.next();

		String result = rmiInterface.printEnrolmentByStudentNumber(studentNumber);
		System.out.println(result);
	}

	private static void enrolment(Scanner scanner, RMIInterface rmiInterface) throws IOException {
		System.out.println("================ [수강 신청] ================");
		System.out.println("학번을 입력하세요.");
		String studentNumber = scanner.next();

		System.out.println("수강 신청할 강의의 과목 코드를 입력하세요.");
		String courseNumber = scanner.next();

		String result = rmiInterface.enrolment(studentNumber, courseNumber);
		System.out.println(result);
	}

	private static void deleteCourse(Scanner scanner, RMIInterface rmiInterface) throws IOException {
		System.out.println("================ [강의 삭제] ================");
		System.out.println("삭제할 강의의 과목 코드를 입력하세요.");
		String courseNumber = scanner.next();

		String result = rmiInterface.deleteCourse(courseNumber);
		System.out.println(result);

	}

	private static void addCourse(Scanner scanner,RMIInterface rmiInterface) throws IOException {
		System.out.println("================ [강의 추가] ================");
		System.out.println("추가할 강의의 과목 코드를 입력하세요.");
		String courseNumber = scanner.next();

		System.out.println("추가할 강의의 교수명을 입력하세요.");
		String professor = scanner.next();

		System.out.println("추가할 강의의 이름을 입력하세요");
		String name = scanner.next();

		System.out.println("해당 과목을 수강하기 위한 선수 과목 수를 입력하세요.");
		int cnt = scanner.nextInt();

		List<String> prerequisiteSubjectList = new LinkedList<>();
		for (int i = 0; i < cnt; i++) {
			System.out.println("선수 과목 코드 " + (i+1) + ":");
			prerequisiteSubjectList.add(scanner.next());
		}

		CreateCourseDto dto = new CreateCourseDto(courseNumber, professor, name, prerequisiteSubjectList);
		String result = rmiInterface.addCourse(dto);
		System.out.println(result);
	}

	private static void addStudent(Scanner scanner, RMIInterface rmiInterface) throws IOException {
		System.out.println("================ [학생 추가] ================");

		System.out.println("추가할 학생의 학번을 입력하세요.");
		String studentNumber = scanner.next();

		System.out.println("추가할 학생의 이름을 입력하세요.");
		String name = scanner.next();

		System.out.println("추가할 학생의 학부을 입력하세요.");
		String department = scanner.next();

		System.out.println("추가할 학생이 수강 완료한 과목 수를 입력하세요.");
		int cnt = scanner.nextInt();

		List<String> completedCoursesList = new LinkedList<>();
		for (int i = 0; i < cnt; i++) {
			System.out.println("과목 코드 " + (i+1) + ":");
			completedCoursesList.add(scanner.next());
		}

		CreateStudentDto dto = new CreateStudentDto(studentNumber, name, department, completedCoursesList);
		String result = rmiInterface.addStudent(dto);
		System.out.println(result);
	}

	private static void deleteStudent(Scanner scanner, RMIInterface rmiInterface) throws IOException {
		System.out.println("================ [학생 삭제] ================");

		System.out.println("삭제할 학생의 학번을 입력하세요.");
		String studentNumber = scanner.next();

		String result = rmiInterface.deleteStudent(studentNumber);
		System.out.println(result);
	}

	private static void printCourseList(RMIInterface rmiInterface) throws IOException {
		System.out.println("================ [강의 목록 조회] ================");
		List<String> courses = rmiInterface.getAllCourses();
		for(String courseInfo : courses){
			System.out.println("==========================================================");
			System.out.println(courseInfo);
		}
	}

	private static void printStudentList(RMIInterface rmiInterface) throws IOException {
		System.out.println("================ [학생 목록 조회] ================");
		List<String> students = rmiInterface.getAllStudents();
		for(String studentInfo : students){
			System.out.println("==========================================================");
			System.out.println(studentInfo);
		}
	}

	private static void signUp(Scanner scanner, RMIInterface rmiInterface) throws IOException {
		System.out.println("================ [회원 가입] ================");
		System.out.println("사용하실 id를 입력하세요. ");
		String id = scanner.next();

		System.out.println("사용하실 password를 입력하세요. ");
		String pwd = scanner.next();

		System.out.println("이름을 입력하세요. ");
		String name = scanner.next();

		String result = rmiInterface.signUp(id, pwd, name);
		System.out.println(result);
	}

	private static void logIn(Scanner scanner, RMIInterface rmiInterface) throws IOException {
		System.out.println("================ [로그인] ================");
		System.out.println("로그인할 id를 입력하세요. ");
		String id = scanner.next();

		System.out.println("로그인할 password를 입력하세요. ");
		String pwd = scanner.next();

		String result = rmiInterface.login(id, pwd);
		System.out.println(result);
	}

	private static void printMenue() {
		System.out.println("================ [수강 신청 프로그램] ================");
		System.out.println("0. 종료");
		System.out.println("1. 회원 가입");
		System.out.println("2. 로그인");
		System.out.println("3. 학생 목록 조회");
		System.out.println("4. 강의 목록 조회");
		System.out.println("5. 학생 추가");
		System.out.println("6. 학생 삭제");
		System.out.println("7. 강의 추가");
		System.out.println("8. 강의 삭제");
		System.out.println("9. 수강 신청");
		System.out.println("10. 수강 신청 내역 확인");

	}
}
