package com.devboot.service;

import com.devboot.dao.StudentRepository;
import com.devboot.entity.Student;
import com.devboot.exception.StudentAlreadyExistsException;
import com.devboot.exception.StudentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentManager implements StudentService{

    private final StudentRepository studentRepository;

    @Autowired
    public StudentManager(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student addStudent(Student student) {
        if(studentAlreadyExists(student.getEmail())) {
            throw new StudentAlreadyExistsException(student.getEmail() + " already exists!");
        }
        return studentRepository.save(student);
    }

    private boolean studentAlreadyExists(String email) {
        return studentRepository.findByEmail(email).isPresent();
    }


    @Override
    public Student updateStudent(Student newStudent, Long id) {

        Optional<Student> student = studentRepository.findById(id.intValue());

        if(student.isPresent()) {
            Student foundStudent = student.get();
            foundStudent.setFirstName(newStudent.getFirstName());
            foundStudent.setLastName(newStudent.getLastName());
            foundStudent.setEmail(newStudent.getEmail());
            foundStudent.setDepartments(newStudent.getDepartments());
            return studentRepository.save(foundStudent);
        } else
             throw new StudentNotFoundException("Sorry, this student could not be found!");

    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id.intValue())
                .orElseThrow(() -> new StudentNotFoundException("Sorry, no student found with the Id: " + id));
    }

    @Override
    public void deleteStudent(Long id) {
       Optional<Student> student = studentRepository.findById(id.intValue());
       if(student.isPresent()) {
           studentRepository.delete(student.get());
       } else
           throw new StudentNotFoundException("Sorry, student not found");
    }
}
