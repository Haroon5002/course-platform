package com.courseplatform.controller;

import com.courseplatform.dto.*;
import com.courseplatform.repository.UserRepository;
import com.courseplatform.service.CourseService;
import com.courseplatform.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Courses", description = "Course browsing and enrollment")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @Operation(summary = "List all courses (Public)")
    public ResponseEntity<CourseListResponse> getAllCourses() {
        CourseListResponse response = courseService.getAllCourses();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}")
    @Operation(summary = "Get course by ID with full details (Public)")
    public ResponseEntity<CourseDetailResponse> getCourseById(@PathVariable String courseId) {
        CourseDetailResponse response = courseService.getCourseById(courseId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{courseId}/enroll")
    @Operation(summary = "Enroll in a course (Requires Authentication)", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<EnrollmentResponse> enrollInCourse(
            @PathVariable String courseId,
            Authentication authentication) {
        String email = authentication.getName();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();

        EnrollmentResponse response = enrollmentService.enrollInCourse(courseId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
