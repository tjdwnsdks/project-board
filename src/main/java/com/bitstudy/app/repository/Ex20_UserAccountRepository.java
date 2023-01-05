package com.bitstudy.app.repository;

import com.bitstudy.app.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Ex20_UserAccountRepository extends JpaRepository<UserAccount, String> {
}
