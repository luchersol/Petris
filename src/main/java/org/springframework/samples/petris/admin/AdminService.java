package org.springframework.samples.petris.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {
    
    AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    @Transactional(readOnly = true)
    public Admin findAdminByUserId(Integer userId){
        return adminRepository.findAdminByUserId(userId);
    }

}
