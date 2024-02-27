package org.springframework.samples.petris.user;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.petris.exceptions.ResourceNotFoundException;
import org.springframework.samples.petris.player.Player;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
class UserServiceTests {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthoritiesService authService;

	@Test
	@WithMockUser(username = "player1", password = "4dm1n")
	void shouldFindCurrentUser() {
		User user = this.userService.findCurrentUser();
		assertEquals("player1", user.getUsername());
	}

	@Test
	@WithMockUser(username = "prueba")
	void shouldNotFindCorrectCurrentUser() {
		assertThrows(ResourceNotFoundException.class, () -> this.userService.findCurrentUser());
	}

	@Test
	void shouldNotFindAuthenticated() {
		assertThrows(ResourceNotFoundException.class, () -> this.userService.findCurrentUser());
	}

	@Test
	void shouldFindAllUsers() {
		List<User> users = this.userService.findAll(PageRequest.of(0, 100)).toList();
		assertEquals(7, users.size());
	}

	@Test
	void shouldFindUsersByUsername() {
		User user = this.userService.findUser("player1");
		assertEquals("player1", user.getUsername());
	}

	@Test
	void shouldFindUsersByAuthority() {
		List<User> owners = (List<User>) this.userService.findAllByAuthority("PLAYER");
		assertEquals(5, owners.size());

		List<User> admins = (List<User>) this.userService.findAllByAuthority("ADMIN");
		assertEquals(2, admins.size());

	}

	@Test
	void shouldNotFindUserByIncorrectUsername() {
		assertThrows(ResourceNotFoundException.class, () -> this.userService.findUser("usernotexists"));
	}


	@Test
	void shouldNotFindSingleUserWithBadID() {
		assertThrows(ResourceNotFoundException.class, () -> this.userService.findUser(100));
	}

	@Test
	void shouldExistUser() {
		assertEquals(true, this.userService.existsUser("player1"));
	}

	@Test
	void shouldNotExistUser() {
		assertEquals(false, this.userService.existsUser("owner10000"));
	}

	@Test
	@Transactional
	void shouldInsertUser() {
		Integer count = this.userService.findAll(PageRequest.of(0, 100000000)).getNumberOfElements();
		User user = new User();
		user.setUsername("Sam");
		user.setPassword("password23");
		user.setEmail("sam@gmail.com");
		user.setAuthority(authService.findByAuthority("ADMIN"));
		user.setOnline(false);
		this.userService.saveUser(user);
		assertNotEquals(0, user.getId().longValue());
		assertNotNull(user.getId());

		Integer finalCount = this.userService.findAll(PageRequest.of(0, 100000000)).getNumberOfElements();
		assertEquals(count + 1, finalCount);
	}
	


	@Test
	@Transactional
	void shouldDeleteUserWithPlayer() {
		Integer count = this.userService.findAll(PageRequest.of(0, 100000000)).getNumberOfElements();
		User user = new User();
		user.setUsername("Sam");
		user.setPassword("password23");
		user.setEmail("sam@gmail.com");
		user.setAuthority(authService.findByAuthority("ADMIN"));
		user.setOnline(false);
		userService.saveUser(user);
		Player p = new Player();
		p.setUser(user);
		Integer secondCount = this.userService.findAll(PageRequest.of(0, 100000000)).getNumberOfElements();
		assertEquals(count + 1, secondCount);
		userService.deleteUser(user.getId());
		Integer lastCount = this.userService.findAll(PageRequest.of(0, 100000000)).getNumberOfElements();
		assertEquals(count, lastCount);
	}
 
}
