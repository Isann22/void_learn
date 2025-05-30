package com.example.voidlearn.controller.rest;



import com.example.voidlearn.dto.CreateUserAdminRequest;
import com.example.voidlearn.model.User;
import com.example.voidlearn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/data")
public class UserRestController {

    private final UserService userService;
    @GetMapping()
    public List<User> getALluser(){
        return userService.getAlluser();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAdminUser(@RequestBody CreateUserAdminRequest userDto
                                  )
    {
        userService.createAdminUser(userDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody CreateUserAdminRequest request, @PathVariable String id) {
        try {
            userService.updateUser(request,id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
