package com.courseplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseSummary {
    private String id;
    private String title;
    private String description;
    private int topicCount;
    private int subtopicCount;
}
