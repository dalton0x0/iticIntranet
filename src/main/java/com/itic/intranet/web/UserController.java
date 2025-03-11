package com.itic.intranet.web;

import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.services.UserService;
import com.itic.intranet.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin(origins = "http://localhost:63343")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public ApiResponse test() {
        return new ApiResponse("API TEST OK !", HttpStatus.OK, null);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getALllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse> getActiveUsers() {
        return ResponseEntity.ok(userService.getAllActiveUsers());
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> findByFirstnameOrLastname(
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname) {
        return ResponseEntity.ok(userService.findByFirstnameOrLastname(firstname, lastname));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(@Valid @RequestBody UserRequestDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userDto));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id,@Valid @RequestBody UserRequestDto userDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, userDto));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
    }

    @DeleteMapping("remove/{id}")
    public ResponseEntity<ApiResponse> removeUser(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.removeUser(id));
    }
}
