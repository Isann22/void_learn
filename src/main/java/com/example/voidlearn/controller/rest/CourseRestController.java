package com.example.voidlearn.controller.rest;


import java.nio.file.Path;
import java.util.List;
import com.example.voidlearn.dao.CourseProjection;
import com.example.voidlearn.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.example.voidlearn.service.CourseService;
import com.example.voidlearn.dto.CourseDto;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
@CrossOrigin()
public class CourseRestController {

    private final CourseService courseService;
    private final StorageService storageService;

    @GetMapping()
    public List<CourseProjection> getAllCourses() {
        return courseService.getCourseData();
    }


    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCourse(
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart("image") MultipartFile image) {

        CourseDto courseDto = new CourseDto();
        courseDto.setTitle(title);
        courseDto.setDescription(description);
        storageService.store(image);
        Path imagePath = storageService.load( image.getOriginalFilename());
        courseDto.setImage(imagePath.toString());
        courseService.createCourse(courseDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            courseService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUser( @RequestPart("title") String title,
                                         @RequestPart("description") String description, @PathVariable String id,  @RequestPart("image") MultipartFile image) {
        try {
            CourseDto courseDto = new CourseDto();
            courseDto.setTitle(title);
            courseDto.setDescription(description);
            storageService.store(image);
            Path imagePath = storageService.load( image.getOriginalFilename());
            courseDto.setImage(imagePath.toString());
            courseService.updateCourse(courseDto,id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
    

}
