package org.springframework.samples.petris.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petris.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthoritiesService {

	private AuthoritiesRepository authoritiesRepository;
//	private UserService userService;

	@Autowired
	public AuthoritiesService(AuthoritiesRepository authoritiesRepository) {
		this.authoritiesRepository = authoritiesRepository;
//		this.userService = userService;
	}

	@Transactional(readOnly = true)
	public Iterable<Authorities> findAll() {
		return this.authoritiesRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Authorities findByAuthority(String authority) {
		return this.authoritiesRepository.findByName(authority)
				.orElseThrow(() -> new ResourceNotFoundException("Authority", "Name", authority));
	}

	@Transactional
	public void saveAuthorities(Authorities authorities) throws DataAccessException {
		authoritiesRepository.save(authorities);
	}

//	@Transactional
//	public void saveAuthorities(String role) throws ResourceNotFoundException {
//		Authorities authority = new Authorities();
//		authority.setAuthority(role);
//		//user.get().getAuthorities().add(authority);
//		authoritiesRepository.save(authority);
//	}

}
