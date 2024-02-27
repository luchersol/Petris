
package org.springframework.samples.petris.auth;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petris.auth.payload.request.LoginRequest;
import org.springframework.samples.petris.auth.payload.request.SignupRequest;
import org.springframework.samples.petris.auth.payload.response.JwtResponse;
import org.springframework.samples.petris.auth.payload.response.MessageResponse;
import org.springframework.samples.petris.configuration.jwt.JwtUtils;
import org.springframework.samples.petris.configuration.services.UserDetailsImpl;
import org.springframework.samples.petris.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "The Authentication API based on JWT")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	private final JwtUtils jwtUtils;
	private final AuthService authService;

	@Autowired
	public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtils jwtUtils,
			AuthService authService) {
		this.userService = userService;
		this.jwtUtils = jwtUtils;
		this.authenticationManager = authenticationManager;
		this.authService = authService;
	}

	@Operation(summary = "Allows you to login into the system", description = "Its the way to get authenticated", tags = {
			"users", "post" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "You are now logged in", content = {
					@Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "422", description = "Bad credentials", content = {
					@Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = {
					@Content(schema = @Schema()) }),
	})
	@PostMapping("/signin")
	public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());
			authService.singIn(userDetails.getId());
			return ResponseEntity.ok()
					.body(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
		} catch (BadCredentialsException exception) {
			return ResponseEntity.badRequest().body("Bad Credentials!");
		}
	}

		@Operation(summary = "Allows you validate your token", description = "Its the way to show you have a correct token", tags = {
			"users", "get" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "This shows if your token is valid or not. It can be true or false", content = {
					@Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = {
					@Content(schema = @Schema()) }),
	})
	@GetMapping("/validate")
	public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
		Boolean isValid = jwtUtils.validateJwtToken(token);
		return ResponseEntity.ok(isValid);
	}

	

	@Operation(summary = "Allows you to register into the system", description = "Its the way to create an account", tags = {
			"users", "post" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "The user has been registered into the system correctly", content = {
					@Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "422", description = "The userName already exists in the system", content = {
					@Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = {
					@Content(schema = @Schema()) }),
	})
	@PostMapping("/signup")	
	public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userService.existsUser(signUpRequest.getUsername()).equals(true)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		authService.createUser(signUpRequest);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

}
