package org.springframework.samples.petris.admin;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminServiceTest {

    @Autowired
    AdminService adminService;

    @Test
    public void shouldFindAdminByUserId(){
        Admin res = adminService.findAdminByUserId(1);
        assertNotNull(res);
        assertTrue(res.getUser().getUsername().equals("admin1"));



    }


    
    @Test
    public void shouldFindAdminByIncorrectUserId(){
        Admin res = adminService.findAdminByUserId(2);
        assertTrue(res == null);

    }
    
}
