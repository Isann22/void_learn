package com.example.voidlearn.controller.rest;



import java.util.List;
import com.example.voidlearn.dao.UserProjection;
import com.example.voidlearn.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.voidlearn.service.UserService;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;

    @GetMapping()
    public List<UserProjection> getALluser(){
        return userService.getUserData();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAdminUser(@RequestBody UserDto request
                                  )
    {
        userService.createAdmin(request);
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
    public ResponseEntity<?> updateUser(@RequestBody UserDto request, @PathVariable String id) {
        try {
            userService.updateUser(request,id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
