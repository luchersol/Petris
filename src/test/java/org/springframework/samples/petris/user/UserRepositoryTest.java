package org.springframework.samples.petris.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository ur;

    @Test
    public void findAllByAuthority(){
        Iterable<User> res = ur.findAllByAuthority("PLAYER");
        assertThat(res).isNotEmpty();
    }

    @Test
    public void findAllByAuthorityNull(){
        Iterable<User> res = ur.findAllByAuthority(null);
        assertThat(res).isEmpty();
    }


    @Test
    public void findAllByAuthorityIncorrect(){
        Iterable<User> res = ur.findAllByAuthority("JUGADOR");
        assertThat(res).isEmpty();
    }
    
}
