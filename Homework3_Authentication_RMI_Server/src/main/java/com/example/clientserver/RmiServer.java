package com.example.clientserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RmiServer extends UnicastRemoteObject implements RMIInterface {
	private MemberRepository memberRepository;
	private StudentRepository studentRepository;
	private CourseRepository courseRepository;
	private EnrolmentRepository enrolmentRepository;

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
	public RmiServer(MemberRepository memberRepository, StudentRepository studentRepository, CourseRepository courseRepository, EnrolmentRepository enrolmentRepository) throws RemoteException {
		super();
		this.memberRepository = memberRepository;
		this.studentRepository = studentRepository;
		this.courseRepository = courseRepository;
		this.enrolmentRepository = enrolmentRepository;
	}

	private void writeAuthenticationLog(String log) throws IOException {
		FileWriter writer = new FileWriter("log.txt", true);
		writer.write(log);
		writer.close();
	}
	// 회원 가입
	@Override
	public String signUp(String memberId, String pwd, String name) throws IOException {
		Optional<Member> member = memberRepository.findByMemberId(memberId);
		if (member.isPresent()){
			writeAuthenticationLog("[사용자 아이디] : " + memberId + " [Command]  : 회원 가입 "  + " [Result] : 실패[id 중복] " + " [Time] : " + LocalDateTime.now() + "\n");
			return memberId + "는 이미 존재하는 id 입니다. 다른 id로 회원가입해주세요.";
		}

		Member newMember = Member.builder()
			.memberId(memberId)
			.name(name)
			.password(pwd)
			.build();

		Member savedMember = memberRepository.save(newMember);

		writeAuthenticationLog("[사용자 아이디] : " + memberId + " [Command]  : 회원 가입 "  + " [Result] : 성공 " + " [Time] : " + LocalDateTime.now() + "\n");
		return savedMember.getName() + "님 회원가입 되었습니다.";
	}

	// 로그인
	@Override
	public String login(String memberId, String pwd) throws IOException {
		Optional<Member> member = memberRepository.findByMemberId(memberId);
		if (member.isPresent()){
			Member loginMember = member.get();
			if (loginMember.getPassword().equals(pwd)){
				writeAuthenticationLog("[사용자 아이디] : " + memberId + " [Command]  : 로그인 "  + " [Result] : 실패[id 중복] " + " [Time] : " + LocalDateTime.now() + "\n");
				return loginMember.getName() + "님 환영합니다.";
			}
		}

		writeAuthenticationLog("[사용자 아이디] : " + memberId + " [Command]  : 로그인 "  + " 실패[유효하지 않은 아이디와 패스워드] " + " [Time] : " + LocalDateTime.now() + "\n");
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

	@Override
	public String addStudent(CreateStudentDto dto) throws RemoteException, IOException {
		Student student = Student.builder()
			.studentNumber(dto.getStudentNumber())
			.name(dto.getName())
			.department(dto.getDepartment())
			.completedCoursesList(dto.getCompletedCoursesList())
			.build();

		Student savedStudent = studentRepository.save(student);
		if (savedStudent.getStudentNumber().equals(student.getStudentNumber())){
			String result = "\n 아래의 학생 정보를 학생 목록에 추가하였습니다. \n";
			result += convertStudentToString(savedStudent);
			return result;
		}

		return "학생 추가에 실패하였습니다. 다시 시도해주세요.";
	}

	@Override
	public String deleteStudent(String studentNumber) throws RemoteException, IOException {
		Optional<Student> optionalStudent = studentRepository.findByStudentNumber(studentNumber);
		if (optionalStudent.isPresent()){
			studentRepository.delete(optionalStudent.get());
			return studentNumber + " 학번을 가진 학생을 성공적으로 삭제하였습니다.";
		}

		return studentNumber + " 학번을 가진 학생은 존재하지 않으므로, 학생 목록에서 삭제할 수 없습니다. 삭제하려는 학생의 학번을 올바르게 입력해주세요.";
	}

	@Override
	public String addCourse(CreateCourseDto dto) throws RemoteException, IOException {
		Course course = Course.builder()
			.courseNumber(dto.getCourseNumber())
			.professor(dto.getProfessor())
			.name(dto.getName())
			.prerequisiteSubjectList(dto.getPrerequisiteSubjectList())
			.build();

		Course savedCourse = courseRepository.save(course);
		if (savedCourse.getCourseNumber().equals(course.getCourseNumber())){
			String result = "\n 아래의 강의 정보를 강의 목록에 추가하였습니다. \n";
			result += convertCourseToString(savedCourse);
			return result;
		}

		return "강의 추가에 실패하였습니다. 다시 시도해주세요.";
	}

	@Override
	public String deleteCourse(String courseNumber) throws RemoteException, IOException {
		Optional<Course> optionalCourse = courseRepository.findByCourseNumber(courseNumber);
		if (optionalCourse.isPresent()){
			Course removedCourse = optionalCourse.get();
			courseRepository.delete(removedCourse);
			return "과목 코드 " +  courseNumber + "의 [ " + removedCourse.getName() + " ] 강의를 성공적으로 삭제하였습니다.";
		}

		return "과목 코드 " + courseNumber + "에 해당하는 강의는 존재하지 않으므로, 강의 목록에서 삭제할 수 없습니다. 삭제하려는 강의의 과목 코드를 올바르게 입력해주세요.";
	}

	@Override
	public String enrolment(String studentNumber, String courseNumber) throws RemoteException, IOException {
		Optional<Student> optionalStudent = studentRepository.findByStudentNumber(studentNumber);
		if(!optionalStudent.isPresent()){
			return "학번 [ " + studentNumber + " ] 에 해당하는 학생이 존재하지 않습니다. 올바른 학번을 입력해주세요. \n";
		}
		Student student = optionalStudent.get();

		Optional<Course> optionalCourse = courseRepository.findByCourseNumber(courseNumber);
		if(!optionalCourse.isPresent()){
			return "과목 코드 [ " + courseNumber + " ] 에 해당하는 강의가 존재하지 않습니다. 올바른 과목 코드를 입력해주세요. \n";
		}
		Course course = optionalCourse.get();

		// 선수 과목이 있다면, 학생이 모든 선수 과목을 이미 수강 완료한 경우에만 수강 신청이 가능하다.
		Set<String> completedCoursesList = new HashSet<>(student.getCompletedCoursesList());
		Set<String> prerequisiteSubjectList = new HashSet<>(course.getPrerequisiteSubjectList());
		// 선수 과목이 있는 경우
		if(!prerequisiteSubjectList.isEmpty()){
			prerequisiteSubjectList.removeAll(completedCoursesList);
			// 학생이 모든 선수 과목을 이미 수강 완료하여 수강 신청에 성공하는 경우
			if(prerequisiteSubjectList.isEmpty()){
				return enrolmentProcess(student, course);
			}
			// 학생이 수강 신청하려는 과목의 선수 과목을 수강하지 않아 수강 신청에 실패하는 경우
			else{
				return "[ " +student.getName() + " ] 학생은 " + course.getName() + "의 선수 과목인 [ " + prerequisiteSubjectList + "] (을)를 수강 완료하지 않아 해당 과목의 수강 신청에 실패하였습니다.";
			}
		}
		// 선수 과목이 없는 경우
		else{
			return enrolmentProcess(student, course);
		}
	}

	private String enrolmentProcess(Student student, Course course) {
		// 이미 수강 신청한 과목이라면 수강 신청에 실패한다.
		Optional<Enrolment> optionalEnrolment = enrolmentRepository.findByStudentNumberAndCourseNumber(student.getStudentNumber(), course.getCourseNumber());
		if(optionalEnrolment.isPresent()){
			return "[ "+ student.getName() + " ] 학생은 [ " + course.getName() + " ] 강의를 이미 수강 신청하였으므로, 중복 수강 신청이 불가능합니다.";

		}

		// 이미 수강 신청한 과목이 아니라면 수강 신청에 성공한다.
		Enrolment enrolment = Enrolment.builder()
			.studentNumber(student.getStudentNumber())
			.courseNumber(course.getCourseNumber())
			.build();
		enrolmentRepository.save(enrolment);
		return "[ " + student.getName() + " ] 학생이 [ " + course.getName() + " ] 강의를 성공적으로 수강 신청하였습니다.";
	}

	@Override
	public String printEnrolmentByStudentNumber(String studentNumber) throws RemoteException {
		Optional<Student> optionalStudent = studentRepository.findByStudentNumber(studentNumber);
		if(!optionalStudent.isPresent()){
			return "학번 [ " + studentNumber + " ] 에 해당하는 학생이 존재하지 않습니다. 올바른 학번을 입력해주세요. \n";
		}
		Student student = optionalStudent.get();

		List<Enrolment> enrolmentList = enrolmentRepository.findAllByStudentNumber(studentNumber);
		if (enrolmentList.isEmpty()){
			return "학번 [ " + studentNumber + " ] 에 해당하는 학생의 수강 신청 내역이 존재하지 않습니다. \n => 신청 과목 수 : [ 0 ]";
		}
		else{
			List<Course> courseList = new ArrayList<>();
			for(Enrolment enrolment : enrolmentList){
				courseList.add(courseRepository.findByCourseNumber(enrolment.getCourseNumber()).get());
			}

			String title = "================ 학번 [ " + studentNumber + " ] 에 해당하는 학생의 수강 신청 내역 ================\n";
			title +=  " => 신청 과목 수 : [ " + courseList.size() + " ] \n";

			List<String> result = courseList.stream()
				.map(this::convertCourseToString)
				.collect(Collectors.toList());

			return title + result;
		}
	}
}
