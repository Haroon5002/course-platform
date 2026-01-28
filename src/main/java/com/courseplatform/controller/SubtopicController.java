package com.courseplatform.controller;

import com.courseplatform.dto.SubtopicCompleteResponse;
import com.courseplatform.repository.UserRepository;
import com.courseplatform.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subtopics")
@Tag(name = "Progress", description = "Track learning progress")
public class SubtopicController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{subtopicId}/complete")
    @Operation(summary = "Mark subtopic as completed (Requires Authentication)", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<SubtopicCompleteResponse> markSubtopicComplete(
            @PathVariable String subtopicId,
            Authentication authentication) {
        String email = authentication.getName();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();

        SubtopicCompleteResponse response = enrollmentService.markSubtopicComplete(subtopicId, userId);
        return ResponseEntity.ok(response);
    }
}
