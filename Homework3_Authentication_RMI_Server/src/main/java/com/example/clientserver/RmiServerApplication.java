package com.example.clientserver;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class RmiServerApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(RmiServerApplication.class, args);
        try {
            RmiServer server = context.getBean(RmiServer.class);
            LocateRegistry.createRegistry(1099); // RMI 레지스트리 시작
            Naming.rebind("AuthenticationServer", server); // 서버를 RMI 레지스트리에 등록
            System.out.println("Authentication Server is running...");
           // System.out.println("Student Data Initialization...");
           // server.initializationStudent("Students.txt");
           // server.initializationCourse("Courses.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
