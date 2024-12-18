package com.tekarch.account_managementMS.repositories;

import com.tekarch.account_managementMS.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
       List<Account> findByUserIdAndAccountType(Long userId, String accountType);
       List<Account> findByAccountId(Long accountId);
       List<Account> findByUserId(Long userId);
       Account findByUserIdAndAccountId(Long userId,Long accountId);

}
