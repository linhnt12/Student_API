package com.example.StudentAPI.repository;

import com.example.StudentAPI.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query("SELECT s FROM Student s WHERE (:gender is null or s.gender = :gender) " +
            "and (:name is null or s.name like %:name%) " +
            "and (:homeTown is null or s.homeTown = :homeTown) " +
            "and (:major is null or s.major = :major) " +
            "and (:minMark is null or s.mark >= :minMark)" +
            "and (:maxMark is null or s.mark <= :maxMark)" +
            "and (:startDate is null or s.dob >= :startDate)" +
            "and (:endDate is null or s.dob <= :endDate)")
    public List<Student> findAll(String name, String gender, String homeTown, String major, Double minMark, Double maxMark, Date startDate, Date endDate);

    @Query("SELECT s FROM Student s WHERE DAY(s.dob) = DAY(CURRENT_DATE) and MONTH(s.dob) = MONTH(CURRENT_DATE)")
    public List<Student> findByBirthday();
}
