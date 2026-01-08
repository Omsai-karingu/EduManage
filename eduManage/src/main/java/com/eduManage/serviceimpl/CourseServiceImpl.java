package com.eduManage.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eduManage.bean.CourseRequest;
import com.eduManage.config.Securityutil;
import com.eduManage.entity.Category;
import com.eduManage.entity.Course;
import com.eduManage.entity.Instructor;
import com.eduManage.entity.User;
import com.eduManage.repository.CategoryRepository;
import com.eduManage.repository.CourseRepository;
import com.eduManage.repository.InstructorRepo;
import com.eduManage.repository.UserRepository;
import com.eduManage.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private InstructorRepo instructorRepository;

    @Autowired
    private UserRepository userRepository;

    // ================= CREATE =================
    @Override
    public ResponseEntity<Object> createCourse(CourseRequest request) {

        String email = Securityutil.getCurrentUser();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Instructor instructor;

        if (user.getRole() == User.Role.INSTRUCTOR) {
            instructor = instructorRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new RuntimeException("Instructor profile not found"));
        } 
        else if (user.getRole() == User.Role.ADMIN) {

            if (request.getInstructorId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Instructor ID is required for admin");
            }

            instructor = instructorRepository.findById(request.getInstructorId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));
        } 
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setPrice(request.getPrice());
        course.setDuration(request.getDuration());
        course.setLessons(request.getLessons());
        course.setLanguage(request.getLanguage());
        course.setCertifications(request.getCertifications());
        course.setImagePath(request.getImagepath());
        course.setCategory(category);
        course.setInstructor(instructor);

        return ResponseEntity.status(HttpStatus.CREATED).body(courseRepository.save(course));
    }

    // ================= UPDATE =================
    @Override
    public ResponseEntity<Course> updateCourse(int id, CourseRequest request) {

        String email = Securityutil.getCurrentUser();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (user.getRole() == User.Role.INSTRUCTOR) {
            Instructor instructor = instructorRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));

            if (course.getInstructor() == null ||
                !course.getInstructor().getId().equals(instructor.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        course.setTitle(request.getTitle());
        course.setPrice(request.getPrice());
        course.setDuration(request.getDuration());
        course.setLessons(request.getLessons());
        course.setLanguage(request.getLanguage());
        course.setCertifications(request.getCertifications());
        course.setImagePath(request.getImagepath());
        course.setCategory(category);

        return ResponseEntity.ok(courseRepository.save(course));
    }

    // ================= DELETE =================
    @Override
    public void delete(int id) {

        String email = Securityutil.getCurrentUser();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Instructor → only own course
        if (user.getRole() == User.Role.INSTRUCTOR) {

            Instructor instructor = instructorRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));

            if (course.getInstructor() == null ||
                !course.getInstructor().getId().equals(instructor.getId())) {
                throw new RuntimeException("You can delete only your own courses");
            }
        }

        // Admin → always allowed
        courseRepository.delete(course);
    }

    // ================= READ =================
    @Override
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course getById(int id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    @Override
    public List<Course> getByCategory(int categoryId) {
        return courseRepository.findByCategory_Id(categoryId);
    }

    @Override
    public List<Course> getByInstructor(int instructorId) {
        return courseRepository.findByInstructor_Id(instructorId);
    }
}
