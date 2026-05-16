package com.student.entity;

import javax.persistence.*;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;
    private String gender;
    private String grade;
    private String major;

    public Student() {}

    public Student(String name, Integer age, String gender, String grade, String major) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.grade = grade;
        this.major = major;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
}
