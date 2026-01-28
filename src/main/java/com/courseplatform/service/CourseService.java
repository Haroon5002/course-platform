package com.courseplatform.service;

import com.courseplatform.dto.*;
import com.courseplatform.entity.Course;
import com.courseplatform.entity.Subtopic;
import com.courseplatform.entity.Topic;
import com.courseplatform.exception.NotFoundException;
import com.courseplatform.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public CourseListResponse getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        List<CourseSummary> summaries = courses.stream()
                .map(course -> {
                    int topicCount = course.getTopics().size();
                    int subtopicCount = course.getTopics().stream()
                            .mapToInt(topic -> topic.getSubtopics().size())
                            .sum();

                    return new CourseSummary(
                            course.getId(),
                            course.getTitle(),
                            course.getDescription(),
                            topicCount,
                            subtopicCount
                    );
                })
                .collect(Collectors.toList());

        return new CourseListResponse(summaries);
    }

    @Transactional(readOnly = true)
    public CourseDetailResponse getCourseById(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course with id '" + courseId + "' does not exist"));

        List<TopicDetail> topics = course.getTopics().stream()
                .map(topic -> {
                    List<SubtopicDetail> subtopics = topic.getSubtopics().stream()
                            .map(subtopic -> new SubtopicDetail(
                                    subtopic.getId(),
                                    subtopic.getTitle(),
                                    subtopic.getContent()
                            ))
                            .collect(Collectors.toList());

                    return new TopicDetail(
                            topic.getId(),
                            topic.getTitle(),
                            subtopics
                    );
                })
                .collect(Collectors.toList());

        return new CourseDetailResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                topics
        );
    }

    @Transactional(readOnly = true)
    public SearchResponse searchCourses(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new SearchResponse(query, new ArrayList<>());
        }

        List<Course> courses = courseRepository.searchCourses(query.trim());

        List<SearchResult> results = courses.stream()
                .map(course -> {
                    List<SearchMatch> matches = new ArrayList<>();
                    String lowerQuery = query.toLowerCase();

                    for (Topic topic : course.getTopics()) {
                        for (Subtopic subtopic : topic.getSubtopics()) {
                            if (subtopic.getTitle().toLowerCase().contains(lowerQuery)) {
                                String snippet = subtopic.getContent().length() > 150
                                        ? subtopic.getContent().substring(0, 150) + "..."
                                        : subtopic.getContent();

                                matches.add(new SearchMatch(
                                        "subtopic",
                                        topic.getTitle(),
                                        subtopic.getId(),
                                        subtopic.getTitle(),
                                        snippet
                                ));
                            } else if (subtopic.getContent().toLowerCase().contains(lowerQuery)) {
                                int index = subtopic.getContent().toLowerCase().indexOf(lowerQuery);
                                int start = Math.max(0, index - 50);
                                int end = Math.min(subtopic.getContent().length(), index + 100);
                                String snippet = (start > 0 ? "..." : "") +
                                        subtopic.getContent().substring(start, end) +
                                        (end < subtopic.getContent().length() ? "..." : "");

                                matches.add(new SearchMatch(
                                        "content",
                                        topic.getTitle(),
                                        subtopic.getId(),
                                        subtopic.getTitle(),
                                        snippet
                                ));
                            }
                        }
                    }

                    return new SearchResult(
                            course.getId(),
                            course.getTitle(),
                            matches
                    );
                })
                .filter(result -> !result.getMatches().isEmpty())
                .collect(Collectors.toList());

        return new SearchResponse(query, results);
    }
}
