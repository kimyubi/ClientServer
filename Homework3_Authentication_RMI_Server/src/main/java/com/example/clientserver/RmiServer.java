package com.example.clientserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RmiServer extends UnicastRemoteObject implements RMIInterface {
	private MemberRepository memberRepository;
	private StudentRepository studentRepository;
	private CourseRepository courseRepository;

	// Student

	protected String studentNumber;
	protected String name;
	protected String department;
	protected List<String> completedCoursesList = new LinkedList<>();
	protected List<Student> studentList = new LinkedList<>();

	// Course
	protected String courseNumber;
	protected String professor;
	protected String courseName;
	protected List<String> prerequisiteSubjectList = new LinkedList<>();;
	protected List<Course> courseList = new LinkedList<>();


	// 초기화
	@Autowired
	public RmiServer(MemberRepository memberRepository, StudentRepository studentRepository, CourseRepository courseRepository) throws RemoteException {
		super();
		this.memberRepository = memberRepository;
		this.studentRepository = studentRepository;
		this.courseRepository = courseRepository;
	}

	// 회원 가입 & 로그인
	@Override
	public String signUp(String memberId, String pwd, String name) throws IOException {
		FileWriter writer = new FileWriter("log.txt", true);

		Optional<Member> member = memberRepository.findByMemberId(memberId);
		if (member.isPresent()){
			writer.write("[사용자 아이디] : " + memberId + " [Command]  : 회원 가입 "  + " [Result] : 실패[id 중복] " + " [Time] : " + LocalDateTime.now() + "\n");

			writer.close();
			return memberId + "는 이미 존재하는 id 입니다. 다른 id로 회원가입해주세요.";
		}

		Member newMember = Member.builder()
			.memberId(memberId)
			.name(name)
			.password(pwd)
			.build();

		Member savedMember = memberRepository.save(newMember);

		writer.write("[사용자 아이디] : " + memberId + " [Command]  : 회원 가입 "  + " [Result] : 성공 " + " [Time] : " + LocalDateTime.now() + "\n");
		writer.close();
		return savedMember.getName() + "님 회원가입 되었습니다.";
	}

	@Override
	public String login(String memberId, String pwd) throws IOException {
		FileWriter writer = new FileWriter("log.txt", true);

		Optional<Member> member = memberRepository.findByMemberId(memberId);
		if (member.isPresent()){
			Member loginMember = member.get();
			if (loginMember.getPassword().equals(pwd)){
				writer.write("[사용자 아이디] : " + memberId + " [Command]  : 로그인 "  + " [Result] : 실패[id 중복] " + " [Time] : " + LocalDateTime.now() + "\n");

				writer.close();

				return loginMember.getName() + "님 환영합니다.";

			}
		}

		writer.write("[사용자 아이디] : " + memberId + " [Command]  : 로그인 "  + " 실패[유효하지 않은 아이디와 패스워드] " + " [Time] : " + LocalDateTime.now() + "\n");
		writer.close();

		return "로그인에 실패하였습니다. 아이디와 비밀번호를 다시 확인해주세요.";
	}

	// 학생 & 강의 데이터 초기화
	@Override
	public void initializationStudent(String fileName) throws IOException {
		BufferedReader studentFile = new BufferedReader(new FileReader(fileName));
		while (studentFile.ready()) {
			String studentInfo = studentFile.readLine();
			if (!studentInfo.equals("")) {
				this.studentList.add(studentFileParser(studentInfo));
			}
		}
		studentFile.close();
		studentRepository.saveAll(studentList);
	}

	private Student studentFileParser(String inputString) {
		StringTokenizer stringTokenizer = new StringTokenizer(inputString);
		this.studentNumber = stringTokenizer.nextToken();
		this.name = stringTokenizer.nextToken();
		this.name = this.name  +  " " + stringTokenizer.nextToken();
		this.department = stringTokenizer.nextToken();
		this.completedCoursesList = new LinkedList<>();
		while (stringTokenizer.hasMoreTokens()) {
			this.completedCoursesList.add(stringTokenizer.nextToken());
		}

		Student student = Student.builder()
			.studentNumber(studentNumber)
			.name(name)
			.department(department)
			.completedCoursesList(completedCoursesList)
			.build();

		return student;
	}

	@Override
	public void initializationCourse(String fileName) throws IOException {
		BufferedReader courseFile = new BufferedReader(new FileReader(fileName));
		while (courseFile.ready()) {
			String courseInfo = courseFile.readLine();
			if (!courseInfo.equals("")) {
				this.courseList.add(courseFileParser(courseInfo));
			}
		}
		courseFile.close();
		courseRepository.saveAll(courseList);
	}

	private Course courseFileParser(String inputString) {
		StringTokenizer stringTokenizer = new StringTokenizer(inputString);
		this.courseNumber = stringTokenizer.nextToken();
		this.professor = stringTokenizer.nextToken();
		this.courseName = stringTokenizer.nextToken();
		this.prerequisiteSubjectList = new LinkedList<>();

		while (stringTokenizer.hasMoreTokens()) {
			this.prerequisiteSubjectList.add(stringTokenizer.nextToken());
		}

		Course course = Course.builder()
			.courseNumber(courseNumber)
			.professor(professor)
			.name(courseName)
			.prerequisiteSubjectList(prerequisiteSubjectList)
			.build();

		return course;
	}

	// 학생 리스트
	@Override
	public List<String> getAllStudents() {
		List<Student> studentList = studentRepository.findAll();
		List<String> result = studentList.stream()
			.map(this::convertStudentToString)
			.collect(Collectors.toList());
		System.out.println(result);
		return result;
	}

	private String convertStudentToString(Student student) {
		StringBuilder sb = new StringBuilder();
		sb.append("학번: ").append(student.getStudentNumber()).append("\n");
		sb.append("이름: ").append(student.getName()).append("\n");
		sb.append("학부: ").append(student.getDepartment()).append("\n");
		sb.append("수강 완료 과목 코드: ").append(student.getCompletedCoursesList().toString()).append("\n");
		sb.append("\n");

		return sb.toString();
	}

	// 강의 목록
	@Override
	public List<String> getAllCourses() throws RemoteException, IOException {
		List<Course> courseList = courseRepository.findAll();
		List<String> result = courseList.stream()
			.map(this::convertCourseToString)
			.collect(Collectors.toList());
		return result;
	}

	private String convertCourseToString(Course course) {
		StringBuilder sb = new StringBuilder();
		sb.append("과목 코드: ").append(course.getCourseNumber()).append("\n");
		sb.append("교수명: ").append(course.getProfessor()).append("\n");
		sb.append("강의명: ").append(course.getName()).append("\n");
		sb.append("선수 강의 과목 코드: ").append(course.getPrerequisiteSubjectList().toString()).append("\n");
		sb.append("\n");

		return sb.toString();
	}




}
