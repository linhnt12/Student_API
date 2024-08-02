package com.example.StudentAPI.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Builder
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name="Student")
public class Student {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(
            name = "student_seq",
            sequenceName = "student_seq",
            allocationSize = 1 //increment by 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_seq"
    )
    private int id;
    @Column(nullable = false, length=100)
    private String name;
    @Column(nullable = false)
    private Date dob;
    @Column(nullable = false, length=100)
    private String className, major, homeTown, gender;
    @Column(nullable = false)
    private double mark;
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    public Student() {
    }

    public Student(String name, String dob, String className, String major, String homeTown, String gender, double mark) throws ParseException {
        this.name = name;
        this.dob = formatter.parse(dob);
        this.className = className;
        this.major = major;
        this.homeTown = homeTown;
        this.gender = gender;
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", className='" + className + '\'' +
                ", major='" + major + '\'' +
                ", homeTown='" + homeTown + '\'' +
                ", gender='" + gender + '\'' +
                ", mark=" + mark +
                '}';
    }
}
