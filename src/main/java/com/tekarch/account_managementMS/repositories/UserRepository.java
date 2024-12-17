package com.tekarch.account_managementMS.repositories;

import com.tekarch.account_managementMS.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
