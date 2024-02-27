package org.springframework.samples.petris.user;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petris.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
class AuthoritiesServiceTests {

	@Autowired
	private AuthoritiesService authService;

	@Test
	void shouldFindAllAuthorities() {
		List<Authorities> auths = (List<Authorities>) this.authService.findAll();
		assertEquals(2, auths.size());
	}

	@Test
	void shouldFindAuthoritiesByAuthority() {
		Authorities auth = this.authService.findByAuthority("PLAYER");
		assertEquals("PLAYER", auth.getAuthority());
	}

	@Test
	void shouldNotFindAuthoritiesByIncorrectAuthority() {
		assertThrows(ResourceNotFoundException.class, () -> this.authService.findByAuthority("authnotexists"));
	}

	@Test
	@Transactional
	void shouldInsertAuthorities() {
		int count = ((Collection<Authorities>) this.authService.findAll()).size();

		Authorities auth = new Authorities();
		auth.setAuthority("CLIENT");

		this.authService.saveAuthorities(auth);
		assertNotEquals(0, auth.getId().longValue());
		assertNotNull(auth.getId());

		int finalCount = ((Collection<Authorities>) this.authService.findAll()).size();
		assertEquals(count + 1, finalCount);
	}


}
