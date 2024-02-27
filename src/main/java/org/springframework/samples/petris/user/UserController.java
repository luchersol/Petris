package org.springframework.samples.petris.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petris.auth.AuthService;
import org.springframework.samples.petris.auth.payload.response.MessageResponse;
import org.springframework.samples.petris.util.RestPreconditions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/petris/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    UserService userService;
    AuthService authService;
    AuthoritiesService authoritiesService;

    @Autowired
    public UserController(UserService userService, AuthService authService, AuthoritiesService authoritiesService) {
        this.userService = userService;
        this.authService = authService;
        this.authoritiesService = authoritiesService;
    }

    @Operation(summary = "Retrives all users", description = "Get a list of users objects", tags = { "users",
            "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User List retrieved", content = {
                    @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping
    public ResponseEntity<List<User>> findAll(@RequestParam(defaultValue = "0") Integer page, 
                                              @RequestParam(defaultValue = "10") Integer size) {
        Pageable paging = PageRequest.of(page, size);
        Page<User> userPage = userService.findAll(paging);
        List<User> body = userPage.getContent();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Retrives an user", description = "Get an user object given his id", tags = { "users",
            "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved", content = {
                    @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "User not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") Integer id) {
        User body = userService.findUser(id);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Retrives all authorities", description = "Get a list of authorities objects", tags = {
            "authorities", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authorities List retrieved", content = {
                    @Content(schema = @Schema(implementation = Authorities.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @GetMapping("/authorities")
    public ResponseEntity<List<Authorities>> findAllAuths() {
        List<Authorities> body = (List<Authorities>) authoritiesService.findAll();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Creates a new user", description = "Create a new user in the system", tags = { "users",
            "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created", content = {
                    @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> create(@RequestBody @Valid User user) {
        User body = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @Operation(summary = "Updates an user", description = "Edit data from an user", tags = { "users", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User edited", content = {
                    @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "User not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@PathVariable("userId") Integer id, @RequestBody @Valid User user) {
        User actualUser = userService.findUser(id);
        RestPreconditions.checkNotNull(actualUser, "User", "ID", id);
        if (userService.existsUser(user.getUsername()).equals(true) && !actualUser.getUsername().equals(user.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        User body = authService.updateUser(user, id);
        return ResponseEntity.ok(body);
    }


      @Operation(summary = "Allows you to log out", description = "Logs out from the system", tags = { "users","put" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Session closed", content = {
                    @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "User not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @PutMapping("/{userId}/logout")
    public void logOut(@PathVariable("userId") Integer userId) {
        authService.logout(userId);
    }

    @Operation(summary = "Deletes an user", description = "Delete an user given his id", tags = { "users", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted", content = {
                    @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "User not found", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MessageResponse> delete(@PathVariable("userId") Integer id) {
        User user = userService.findUser(id);
        RestPreconditions.checkNotNull(user, "User", "ID", id);
        userService.deleteUser(id);
        MessageResponse body = new MessageResponse("User deleted!");
        return ResponseEntity.ok(body);
    }
}
