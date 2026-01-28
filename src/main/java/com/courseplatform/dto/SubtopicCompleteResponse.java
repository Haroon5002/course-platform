package com.courseplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SubtopicCompleteResponse {
    private String subtopicId;
    private Boolean completed;
    private LocalDateTime completedAt;
}
