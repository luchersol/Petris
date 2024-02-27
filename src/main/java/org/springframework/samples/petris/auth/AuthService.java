
package org.springframework.samples.petris.auth;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petris.auth.payload.request.SignupRequest;
import org.springframework.samples.petris.player.Player;
import org.springframework.samples.petris.player.PlayerService;
import org.springframework.samples.petris.stats.Stats;
import org.springframework.samples.petris.stats.StatsService;
import org.springframework.samples.petris.user.Authorities;
import org.springframework.samples.petris.user.AuthoritiesService;
import org.springframework.samples.petris.user.User;
import org.springframework.samples.petris.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class AuthService {

	private final PasswordEncoder encoder;
	private final AuthoritiesService authoritiesService;
	private final UserService userService;
	private final PlayerService playerService;
	private final StatsService statsService;

	private final String ADMIN = "ADMIN";
	private final String PLAYER = "PLAYER";

	@Autowired
	public AuthService(PasswordEncoder encoder, AuthoritiesService authoritiesService, UserService userService,
	PlayerService playerService, StatsService statsService) {
		this.encoder = encoder;
		this.authoritiesService = authoritiesService;
		this.userService = userService;
		this.playerService = playerService;
		this.statsService = statsService;
	}

	@Transactional
	public User updateUser(User user, Integer idToUpdate) {
		User toUpdate = userService.findUser(idToUpdate);
		BeanUtils.copyProperties(user, toUpdate, "id");
		String password = user.getPassword();
		toUpdate.setPassword(encoder.encode(password));
		userService.saveUser(toUpdate);
		return toUpdate;
	}

	@Transactional
	public void logout(@PathVariable("userId") Integer userId) {
        User user = userService.findUser(userId);
        user.setOnline(false);
		User toUpdate = userService.findUser(userId);
		BeanUtils.copyProperties(user, toUpdate, "id");
		userService.saveUser(toUpdate);
    }

	@Transactional
	public void singIn(@PathVariable("userId") Integer userId) {
        User user = userService.findUser(userId);
        user.setOnline(true);
		User toUpdate = userService.findUser(userId);
		BeanUtils.copyProperties(user, toUpdate, "id");
		userService.saveUser(toUpdate);
    }


	@Transactional
	public void createUser(@Valid SignupRequest request) {
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(encoder.encode(request.getPassword()));
		user.setEmail(request.getEmail());
		String strRoles = request.getAuthority();
		Authorities role;
		switch (strRoles.toLowerCase()) {
		case "admin":
			role = authoritiesService.findByAuthority(ADMIN);
			user.setAuthority(role);
			user.setOnline(true);
			userService.saveUser(user);
			break;
		case "player":
			role = authoritiesService.findByAuthority(PLAYER);
			user.setAuthority(role);
			user.setOnline(true);
			userService.saveUser(user);
			Player player = new Player();
			player.setUser(user);
			Stats stats = Stats.initialStats();
			statsService.saveStats(stats);
			player.setStats(stats);
			playerService.savePlayer(player);
			break;
		}
	}

}
