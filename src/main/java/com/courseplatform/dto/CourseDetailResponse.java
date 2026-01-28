package com.courseplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class CourseDetailResponse {
    private String id;
    private String title;
    private String description;
    private List<TopicDetail> topics;
}
