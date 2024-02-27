package org.springframework.samples.petris.admin;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Integer>{

    @Query("SELECT admin FROM Admin admin WHERE admin.user.id = :userId")
    Admin findAdminByUserId(Integer userId);
    
}
