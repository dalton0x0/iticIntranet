package com.itic.intranet.web;

import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.models.User;
import com.itic.intranet.services.UserService;
import com.itic.intranet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin(origins = "http://localhost:63343")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public ResponseEntity<ApiResponse> test() {
        return ResponseEntity.ok(new ApiResponse("API TEST OK !", null));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return allUsers.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("No users found", allUsers))
                : ResponseEntity.ok(new ApiResponse("List of all users", allUsers));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse> getActiveUsers() {
        return userService.getAllActiveUsers();
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> findByFirstnameOrLastname(
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname) {
        return userService.findByFirstnameOrLastname(firstname, lastname);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(@RequestBody UserRequestDto userDto) {
        return userService.addUser(userDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody UserRequestDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ApiResponse> removeUser(@PathVariable Long id) {
        return userService.removeUser(id);
    }
}
