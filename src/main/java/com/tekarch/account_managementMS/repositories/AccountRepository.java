package com.tekarch.account_managementMS.repositories;

import com.tekarch.account_managementMS.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
   //    List<Account> findByUserUserIdAndAccountId(Long userId, Long accountId);
       List<Account> findByAccountId(Long accountId);
       List<Account> findByUserUserId(Long userId);

}
