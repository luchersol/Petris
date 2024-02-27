package org.springframework.samples.petris.user;

import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AuthoritiesRepositoryTest {

    @Autowired
    AuthoritiesRepository ar;

    @Test
    public void findByName(){
        Optional<Authorities> res = ar.findByName("PLAYER");
        assumeTrue(res.isPresent());
        assertEquals(res.get().getAuthority(),"PLAYER");
        assertEquals(res.get().getId(),2);
    }

    @Test
    public void findByNameIncorrect(){
        Optional<Authorities> res = ar.findByName("DOCTOR");
        assertTrue(res.isEmpty());

    }
    
}
