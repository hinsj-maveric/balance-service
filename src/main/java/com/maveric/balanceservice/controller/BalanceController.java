package com.maveric.balanceservice.controller;

import com.maveric.balanceservice.dto.AccountDto;
import com.maveric.balanceservice.dto.BalanceDto;
import com.maveric.balanceservice.exception.AccountIdMismatchException;
import com.maveric.balanceservice.exception.BalanceIdNotFoundException;
import com.maveric.balanceservice.exception.CustomerIDNotFoundExistsException;
import com.maveric.balanceservice.feignclient.AccountFeignService;
import com.maveric.balanceservice.service.BalanceService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BalanceController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BalanceController.class);

    @Autowired
    BalanceService balanceService;

    @Autowired
    AccountFeignService accountFeignService;


    @GetMapping("accounts/{accountId}/balances/{balanceId}")
    public ResponseEntity<BalanceDto> getBalanceByAccountId(@PathVariable("accountId") String accountId,
                                                            @PathVariable("balanceId") String balanceId)
            throws BalanceIdNotFoundException, AccountIdMismatchException {

        return new ResponseEntity<>(balanceService.getBalanceIdByAccountId(accountId, balanceId), HttpStatus.OK);
    }

    @GetMapping("accounts/{accountId}/balances")
    public List<BalanceDto> getAllBalanceByAccountId(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                         @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize, @PathVariable("accountId")@Valid String accountId)throws BalanceIdNotFoundException {

        return balanceService.getBalanceByAccountId(page, pageSize, accountId);}



    @DeleteMapping("accounts/{accountId}/balances/{balanceId}")
    public ResponseEntity<String> deleteBalanceByAccountId(@PathVariable("accountId") String accountId,
                                                           @PathVariable("balanceId") String balanceId)
            throws BalanceIdNotFoundException, AccountIdMismatchException {
        balanceService.deleteBalanceByAccountId(accountId, balanceId);
        return new ResponseEntity<>("Balance deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/accounts/{accountId}/balances/{balanceId}")
    public ResponseEntity<BalanceDto> updateBalance(@Valid @RequestBody BalanceDto balanceDto, @PathVariable String accountId, @PathVariable String balanceId) {

        BalanceDto balanceDetails = balanceService.updateBalance(accountId, balanceId, balanceDto);
        return ResponseEntity.status(HttpStatus.OK).body(balanceDetails);

    }
    @PostMapping("accounts/{accountId}/balances")
    public ResponseEntity<BalanceDto> createBalance(@PathVariable String accountId,
                                                        @Valid @RequestBody BalanceDto balanceDto,
                                                    HttpServletRequest request) throws AccountNotFoundException {
        String customerId = (String) request.getHeader("userid");

        if(customerId == null){
            log.info("API call to create a new Balance for given Account Id");
            BalanceDto balanceDtoResponse = balanceService.createBalance(accountId, balanceDto);
            log.info("New Balance Created successfully");
            return new ResponseEntity<>(balanceDtoResponse, HttpStatus.CREATED);
        }
        else {
            AccountDto accountDto = accountFeignService.getAccount(customerId, accountId);
            if(accountDto != null) {
                log.info("API call to create a new Balance for given Account Id");
                BalanceDto balanceDtoResponse = balanceService.createBalance(accountId, balanceDto);
                log.info("New Balance Created successfully");
                return new ResponseEntity<>(balanceDtoResponse, HttpStatus.CREATED);
            } else {
                throw new AccountNotFoundException("Account not found");
            }
        }



    }

}

