package org.springframework.samples.petris.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.petris.auth.payload.request.SignupRequest;
import org.springframework.samples.petris.player.Player;
import org.springframework.samples.petris.player.PlayerService;
import org.springframework.samples.petris.user.AuthoritiesService;
import org.springframework.samples.petris.user.User;
import org.springframework.samples.petris.user.UserService;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class AuthServiceTests {

	@Autowired
	protected AuthService authService;
	@Autowired
	protected UserService userService;
	@Autowired
	protected AuthoritiesService authoritiesService;
	@Autowired
	protected PlayerService playerService;

	@Test
	@Transactional
	public void shouldCreateAdminUser() {
		SignupRequest request = createRequest("ADMIN", "admin3");
		Integer count = this.userService.findAll(PageRequest.of(0, 100000000)).getNumberOfElements();
		this.authService.createUser(request);
		Integer secondCount = this.userService.findAll(PageRequest.of(0, 100000000)).getNumberOfElements();
		assertEquals(count + 1 , secondCount);
	}
	
	
	@Test
	@Transactional
	public void shouldCreatePlayerUser() {
		SignupRequest request = createRequest("PLAYER", "playerTest");
		Integer userFirstCount = this.userService.findAll(PageRequest.of(0, 100000000)).getNumberOfElements();
		Integer playerFirstCount = ((Collection<Player>) this.playerService.findAllPlayers()).size();
		this.authService.createUser(request);
		Integer userLastCount = this.userService.findAll(PageRequest.of(0, 100000000)).getNumberOfElements();
		Integer playerLastCount = ((Collection<Player>) this.playerService.findAllPlayers()).size();
		assertEquals(userFirstCount + 1, userLastCount);
		assertEquals(playerFirstCount + 1, playerLastCount);
	}

	private SignupRequest createRequest(String auth, String username) {
		SignupRequest request = new SignupRequest();
		request.setEmail("prueba@gmail.com");
		request.setAuthority(auth);
		request.setPassword("prueba");
		request.setUsername(username);
		return request;
	}

	@Test
	@Transactional
	void shouldUpdateUser() {
		User user = this.userService.findUser(2);
		user.setUsername("Change");
		authService.updateUser(user, 2);
		user = this.userService.findUser(2);
		assertEquals("Change", user.getUsername());
	}

}
