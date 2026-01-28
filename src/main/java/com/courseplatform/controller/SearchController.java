package com.courseplatform.controller;

import com.courseplatform.dto.SearchResponse;
import com.courseplatform.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@Tag(name = "Search", description = "Search courses and content")
public class SearchController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    @Operation(summary = "Search courses by keyword (Public)")
    public ResponseEntity<SearchResponse> searchCourses(@RequestParam String q) {
        SearchResponse response = courseService.searchCourses(q);
        return ResponseEntity.ok(response);
    }
}
