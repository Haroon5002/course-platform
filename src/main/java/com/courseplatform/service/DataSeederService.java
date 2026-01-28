package com.courseplatform.service;

import com.courseplatform.entity.Course;
import com.courseplatform.entity.Subtopic;
import com.courseplatform.entity.Topic;
import com.courseplatform.repository.CourseRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class DataSeederService implements CommandLineRunner {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        if (courseRepository.count() > 0) {
            System.out.println("Database already contains data. Skipping seed.");
            return;
        }

        System.out.println("Loading seed data...");

        ClassPathResource resource = new ClassPathResource("seed-data.json");
        InputStream inputStream = resource.getInputStream();
        JsonNode rootNode = objectMapper.readTree(inputStream);
        JsonNode coursesNode = rootNode.get("courses");

        for (JsonNode courseNode : coursesNode) {
            Course course = new Course();
            course.setId(courseNode.get("id").asText());
            course.setTitle(courseNode.get("title").asText());
            course.setDescription(courseNode.get("description").asText());

            JsonNode topicsNode = courseNode.get("topics");
            for (JsonNode topicNode : topicsNode) {
                Topic topic = new Topic();
                topic.setId(topicNode.get("id").asText());
                topic.setTitle(topicNode.get("title").asText());
                topic.setCourse(course);

                JsonNode subtopicsNode = topicNode.get("subtopics");
                for (JsonNode subtopicNode : subtopicsNode) {
                    Subtopic subtopic = new Subtopic();
                    subtopic.setId(subtopicNode.get("id").asText());
                    subtopic.setTitle(subtopicNode.get("title").asText());
                    subtopic.setContent(subtopicNode.get("content").asText());
                    subtopic.setTopic(topic);

                    topic.getSubtopics().add(subtopic);
                }

                course.getTopics().add(topic);
            }

            courseRepository.save(course);
            System.out.println("Loaded course: " + course.getTitle());
        }

        System.out.println("Seed data loaded successfully!");
    }
}
