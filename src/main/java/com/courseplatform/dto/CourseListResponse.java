package com.courseplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class CourseListResponse {
    private List<CourseSummary> courses;
}
