package com.maveric.balanceservice.feignclient;

import com.maveric.balanceservice.dto.AccountDto;
import com.maveric.balanceservice.entity.Account;
import com.maveric.balanceservice.exception.CustomerIDNotFoundExistsException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "feignAccount",url = "http://localhost:3010/api/v1/")
public interface AccountFeignService {
    @GetMapping("/customers/{customerId}/customerAccounts")
    ResponseEntity<List<Account>> getAccountsbyId(@PathVariable String customerId);

    @GetMapping("customers/{customerId}/accounts/{accountId}")
    public AccountDto getAccount(@PathVariable("customerId") String customerId,
                                 @Valid @PathVariable("accountId") String accountId,
                                 @RequestHeader(value = "userid") String headerUserId) throws AccountNotFoundException ,CustomerIDNotFoundExistsException;
}
