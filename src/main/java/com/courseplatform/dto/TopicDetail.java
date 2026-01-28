package com.courseplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class TopicDetail {
    private String id;
    private String title;
    private List<SubtopicDetail> subtopics;
}
