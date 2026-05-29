package com.lab4.demo.user;

import com.lab4.demo.security.AuthService;
import com.lab4.demo.security.dto.JwtResponse;
import com.lab4.demo.security.dto.MessageResponse;
import com.lab4.demo.user.dto.UserListDTO;
import com.lab4.demo.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

import static com.lab4.demo.UrlMapping.USER;
import static com.lab4.demo.UrlMapping.USER_ID_PART;

@Validated
@RestController
@CrossOrigin
@RequestMapping(USER)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserListDTO> allUsers() {
        return userService.allUsersForList();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserListDTO user) {
        if (userService.existsByUsername(user.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        userService.createUser(user);
        return ResponseEntity.ok(new MessageResponse("User created"));
    }

    @DeleteMapping(USER_ID_PART)
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new MessageResponse("User deleted"));
    }

    @PutMapping(USER_ID_PART)
    public ResponseEntity<?> updateUser(@PathVariable Long id,@Valid @RequestBody UserListDTO user) {
//        if (userService.existsByUsername(user.getName())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Username is already taken!"));
//        }
//        if (userService.existsByEmail(user.getEmail())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is already in use!"));
//        }
        try {
            userService.updateUser(id,user);
        } catch (UserNotFoundException e) {

            e.printStackTrace();
        }
        return ResponseEntity.ok(new MessageResponse("User updated"));
    }


}
