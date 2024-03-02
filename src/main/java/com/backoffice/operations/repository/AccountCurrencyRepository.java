package com.backoffice.operations.repository;

import com.backoffice.operations.entity.AccessToken;
import com.backoffice.operations.entity.AccountCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCurrencyRepository extends JpaRepository<AccountCurrency, String> {

    AccountCurrency findByAccountCurrencyCode(String omr);
}
