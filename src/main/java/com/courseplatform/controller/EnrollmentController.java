package com.courseplatform.controller;

import com.courseplatform.dto.ProgressResponse;
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
@RequestMapping("/api/enrollments")
@Tag(name = "Progress", description = "Track learning progress")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{enrollmentId}/progress")
    @Operation(summary = "View progress for an enrollment (Requires Authentication)", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ProgressResponse> getProgress(
            @PathVariable Long enrollmentId,
            Authentication authentication) {
        String email = authentication.getName();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();

        ProgressResponse response = enrollmentService.getProgress(enrollmentId, userId);
        return ResponseEntity.ok(response);
    }
}
