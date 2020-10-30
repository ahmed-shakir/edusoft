package se.supernovait.edusoft.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.supernovait.edusoft.entities.Course;

import java.net.URI;
import java.util.List;

/**
 * <description>
 *
 * @author Ahmed Shakir
 * @version 1.0
 * @since 2020-10-18
 */
@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    @GetMapping
    public ResponseEntity<List<Course>> findAllCourses() {
        return ResponseEntity.ok(List.of(Course.builder().name("Test course").build()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> findCourseById(@PathVariable String id) {
        return ResponseEntity.ok(Course.builder().name("Test course").build());
    }

    @PostMapping
    public ResponseEntity<Course> saveCourse(@RequestBody Course course) {
        return ResponseEntity.created(URI.create("uri")).body(Course.builder().name("Test course").build());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCourse(@PathVariable String id, @RequestBody Course course) {

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable String id) {

    }
}
