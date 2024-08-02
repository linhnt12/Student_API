package com.example.StudentAPI.controller;

import com.example.StudentAPI.model.ResponseObject;
import com.example.StudentAPI.model.Student;
import com.example.StudentAPI.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping(path = "/api/v1/students", method = {RequestMethod.GET, RequestMethod.POST})
public class StudentController {
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> search(@Param("name") String name, @Param("gender") String gender, @Param("homeTown") String homeTown, @Param("major") String major, @Param("minMark") String minMark, @Param("maxMark") String maxMark, @Param("startDate") String startDate, @Param("endDate") String endDate) throws ParseException {
        if (minMark == null) minMark = "0.0";
        if (maxMark == null) maxMark = "10.0";
        if (startDate == "" || startDate == null) startDate = "01-Jan-1000";
        if (endDate == "" || endDate == null) endDate = "31-Dec-3000";
        if (studentRepository.findAll(name, gender, homeTown, major, Double.parseDouble(minMark), Double.parseDouble(maxMark), formatter.parse(startDate), formatter.parse(endDate)).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("not found", "Cannot find suitable student", null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query successful", studentRepository.findAll(name, gender, homeTown, major, Double.parseDouble(minMark), Double.parseDouble(maxMark), formatter.parse(startDate), formatter.parse(endDate)))
        );
    }

    @GetMapping("/birthday")
    ResponseEntity<ResponseObject> birthday() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query successful", studentRepository.findByBirthday())
        );
    }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertStudent(@RequestBody Student newStudent) {
        if (newStudent.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new ResponseObject("not acceptable", "Name cannot be empty", null)
            );
        }
        if (newStudent.getName().length() > 51) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new ResponseObject("not acceptable", "Name cannot be longer than 50 characters", null)
            );
        }
        if (newStudent.getDob() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new ResponseObject("not acceptable", "Date of birth cannot be empty", null)
            );
        }
        if (newStudent.getClassName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new ResponseObject("not acceptable", "Class name cannot be empty", null)
            );
        }
        if (newStudent.getMajor().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new ResponseObject("not acceptable", "Major cannot be empty", null)
            );
        }
        if (newStudent.getGender().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new ResponseObject("not acceptable", "Major cannot be empty", null)
            );
        }
        if (newStudent.getHomeTown().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new ResponseObject("not acceptable", "Home town cannot be empty", null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert successful", studentRepository.save(newStudent))
        );
    }

    @PatchMapping("/{id}")
    ResponseEntity<ResponseObject> updateStudent(@PathVariable int id, @RequestBody Student newStudent) {
        if (newStudent.getName().length() > 51) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new ResponseObject("not acceptable", "Name cannot be longer than 50 characters", null)
            );
        }
        Student updatedStudent = studentRepository.findById(id)
                .map(student -> {
                    student.setName(newStudent.getName());
                    student.setDob(newStudent.getDob());
                    student.setClassName(newStudent.getClassName());
                    student.setMajor(newStudent.getMajor());
                    student.setGender(newStudent.getGender());
                    student.setHomeTown(newStudent.getHomeTown());
                    student.setMark(newStudent.getMark());
                    return studentRepository.save(student);
                }).orElseGet(()-> {
                    newStudent.setId(id);
                    return studentRepository.save(newStudent);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update successful", updatedStudent)
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteStudent(@PathVariable int id) {
        boolean exists = studentRepository.existsById(id);
        if (exists) {
            studentRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete successful", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("not found", "Cannot find student with id = " + id, "")
        );
    }
}
