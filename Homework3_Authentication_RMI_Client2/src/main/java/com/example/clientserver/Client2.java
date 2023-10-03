package com.example.clientserver;

import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

public class Client2 {
	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			RMIInterface rmiInterface = (RMIInterface) Naming.lookup("rmi://localhost/AuthenticationServer");

			System.out.println("================ [수강 신청 프로그램] ================");
			System.out.println("1. 회원 가입");
			System.out.println("2. 로그인");
			System.out.println("3. 학생 목록 조회");
			System.out.println("4. 강의 목록 조회");

			int value = scanner.nextInt();

			if(value == 1){
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

			else if(value == 2){
				System.out.println("================ [로그인] ================");
				System.out.println("로그인할 id를 입력하세요. ");
				String id = scanner.next();

				System.out.println("로그인할 password를 입력하세요. ");
				String pwd = scanner.next();

				String result = rmiInterface.login(id, pwd);
				System.out.println(result);

			}
			
			else if(value ==3){
				System.out.println("================ [학생 목록 조회] ================");

				List<String> students = rmiInterface.getAllStudents();
				for(String studentInfo : students){
					System.out.println("==========================================================");
					System.out.println(studentInfo);
				}
			}

			else if(value ==4){
				System.out.println("================ [강의 목록 조회] ================");
				List<String> courses = rmiInterface.getAllCourses();
				for(String courseInfo : courses){
					System.out.println("==========================================================");
					System.out.println(courseInfo);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
