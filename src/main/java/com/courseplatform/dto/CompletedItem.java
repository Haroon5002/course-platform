package com.courseplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CompletedItem {
    private String subtopicId;
    private String subtopicTitle;
    private LocalDateTime completedAt;
}
