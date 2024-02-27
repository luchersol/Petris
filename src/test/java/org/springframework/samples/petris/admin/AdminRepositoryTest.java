package org.springframework.samples.petris.admin;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AdminRepositoryTest {

    @Autowired
    AdminRepository ar;
    
    @ParameterizedTest
    @CsvSource({"1, admin1","4, admin2"})
     public void findAdminByUserId(int id, String username){
        Admin res = ar.findAdminByUserId(id);
        assertNotNull(res);
        assertEquals(res.getUser().getUsername(), username);
     }

    @Test
    public void findAdminByIncorrectUserId(){
        Admin res = ar.findAdminByUserId(null);
        assertNull(res);
     }
    


    
}
