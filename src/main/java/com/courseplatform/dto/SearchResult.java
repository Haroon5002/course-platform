package com.courseplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class SearchResult {
    private String courseId;
    private String courseTitle;
    private List<SearchMatch> matches;
}
