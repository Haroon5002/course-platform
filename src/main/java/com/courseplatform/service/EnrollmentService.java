package com.courseplatform.service;

import com.courseplatform.dto.*;
import com.courseplatform.entity.*;
import com.courseplatform.exception.ConflictException;
import com.courseplatform.exception.ForbiddenException;
import com.courseplatform.exception.NotFoundException;
import com.courseplatform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubtopicRepository subtopicRepository;

    @Autowired
    private SubtopicProgressRepository subtopicProgressRepository;

    @Transactional
    public EnrollmentResponse enrollInCourse(String courseId, Long userId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course with id '" + courseId + "' does not exist"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (enrollmentRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new ConflictException("You are already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);

        enrollment = enrollmentRepository.save(enrollment);

        return new EnrollmentResponse(
                enrollment.getId(),
                course.getId(),
                course.getTitle(),
                enrollment.getEnrolledAt()
        );
    }

    @Transactional
    public SubtopicCompleteResponse markSubtopicComplete(String subtopicId, Long userId) {
        Subtopic subtopic = subtopicRepository.findById(subtopicId)
                .orElseThrow(() -> new NotFoundException("Subtopic with id '" + subtopicId + "' does not exist"));

        String courseId = subtopic.getTopic().getCourse().getId();

        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new ForbiddenException("You must be enrolled in this course to mark subtopics as complete"));

        SubtopicProgress progress = subtopicProgressRepository
                .findByEnrollmentIdAndSubtopicId(enrollment.getId(), subtopicId)
                .orElse(new SubtopicProgress());

        if (progress.getId() == null) {
            progress.setEnrollment(enrollment);
            progress.setSubtopic(subtopic);
        }

        progress.setCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());

        progress = subtopicProgressRepository.save(progress);

        return new SubtopicCompleteResponse(
                subtopicId,
                progress.getCompleted(),
                progress.getCompletedAt()
        );
    }

    @Transactional(readOnly = true)
    public ProgressResponse getProgress(Long enrollmentId, Long userId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new NotFoundException("Enrollment not found"));

        if (!enrollment.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only view your own progress");
        }

        Course course = enrollment.getCourse();

        int totalSubtopics = course.getTopics().stream()
                .mapToInt(topic -> topic.getSubtopics().size())
                .sum();

        List<SubtopicProgress> progressRecords = subtopicProgressRepository.findByEnrollmentId(enrollmentId);

        int completedSubtopics = (int) progressRecords.stream()
                .filter(SubtopicProgress::getCompleted)
                .count();

        double completionPercentage = totalSubtopics > 0
                ? Math.round((completedSubtopics * 100.0 / totalSubtopics) * 100.0) / 100.0
                : 0.0;

        List<CompletedItem> completedItems = progressRecords.stream()
                .filter(SubtopicProgress::getCompleted)
                .map(progress -> new CompletedItem(
                        progress.getSubtopic().getId(),
                        progress.getSubtopic().getTitle(),
                        progress.getCompletedAt()
                ))
                .collect(Collectors.toList());

        return new ProgressResponse(
                enrollmentId,
                course.getId(),
                course.getTitle(),
                totalSubtopics,
                completedSubtopics,
                completionPercentage,
                completedItems
        );
    }
}
