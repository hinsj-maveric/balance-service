package com.maveric.balanceservice.controller;

import com.maveric.balanceservice.constant.MessageConstant;
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

import javax.validation.Valid;

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
                                                            @PathVariable("balanceId") String balanceId,
                                                            @RequestHeader(value = "userid") String headerUserId)
            throws BalanceIdNotFoundException, AccountIdMismatchException {
        AccountDto accountDto = accountFeignService.getAccountByUserId(accountId, headerUserId);
        if(accountDto != null){
            return new ResponseEntity<>(balanceService.getBalanceIdByAccountId(accountId, balanceId), HttpStatus.OK);
        }
        else{
            throw new CustomerIDNotFoundExistsException(MessageConstant.NOT_AUTHORIZED_USER);
        }
    }

    @GetMapping("accounts/{accountId}/balances")
    public ResponseEntity<BalanceDto> getAllBalanceByAccountId(@PathVariable("accountId") @Valid String accountId,
                                               @RequestHeader(value = "userid") String headerUserId) throws BalanceIdNotFoundException {
        AccountDto accountDto = accountFeignService.getAccountByUserId(accountId, headerUserId);
        if(accountDto != null){
            BalanceDto balanceDto = balanceService.getBalanceByAccountId(accountId);
            return new ResponseEntity<>(balanceDto, HttpStatus.OK);
        }
        else{
            throw new CustomerIDNotFoundExistsException(MessageConstant.NOT_AUTHORIZED_USER);
        }
    }



    @DeleteMapping("accounts/{accountId}/balances/{balanceId}")
    public ResponseEntity<String> deleteBalanceByAccountId(@PathVariable("accountId") String accountId,
                                                           @PathVariable("balanceId") String balanceId,
                                                           @RequestHeader(value = "userid") String headerUserId)
            throws BalanceIdNotFoundException, AccountIdMismatchException {
        AccountDto accountDto = accountFeignService.getAccountByUserId(accountId, headerUserId);
        if(accountDto != null){
            balanceService.deleteBalanceByAccountId(accountId, balanceId);
            return new ResponseEntity<>("Balance deleted successfully", HttpStatus.OK);
        }
        else{
            throw new CustomerIDNotFoundExistsException(MessageConstant.NOT_AUTHORIZED_USER);
        }
    }

    @PutMapping("/accounts/{accountId}/balances/{balanceId}")
    public ResponseEntity<BalanceDto> updateBalance(@Valid @RequestBody BalanceDto balanceDto,
                                                    @PathVariable String accountId,
                                                    @PathVariable String balanceId,
                                                    @RequestHeader(value = "userid") String headerUserId) {
        AccountDto accountDto = accountFeignService.getAccountByUserId(accountId, headerUserId);
        if(accountDto != null){
            BalanceDto balanceDetails = balanceService.updateBalance(accountId, balanceId, balanceDto);
            return ResponseEntity.status(HttpStatus.OK).body(balanceDetails);
        }
        else {
            throw new CustomerIDNotFoundExistsException(MessageConstant.NOT_AUTHORIZED_USER);
        }

    }
    @PostMapping("accounts/{accountId}/balances")
    public ResponseEntity<BalanceDto> createBalance(@PathVariable String accountId,
                                                    @Valid @RequestBody BalanceDto balanceDto,
                                                    @RequestHeader(value = "userid") String headerUserId) {

        AccountDto accountDto = accountFeignService.getAccountByUserId(accountId, headerUserId);
        if(accountDto != null) {
            log.info("API call to create a new Balance for given Account Id");
            BalanceDto balanceDtoResponse = balanceService.createBalance(accountId, balanceDto);
            log.info("New Balance Created successfully");
            return new ResponseEntity<>(balanceDtoResponse, HttpStatus.CREATED);
        }
        else {
            throw new CustomerIDNotFoundExistsException(MessageConstant.NOT_AUTHORIZED_USER);
        }
    }

    @DeleteMapping("/accounts/{accountId}/balances")
    public ResponseEntity<String> deleteBalanceByAccountId(@PathVariable("accountId") String accountId,
                                                           @RequestHeader(value = "userid") String headerUserId) {
        AccountDto accountDto = accountFeignService.getAccountByUserId(accountId, headerUserId);
        if(accountDto != null) {
            balanceService.deleteBalanceByAccountId(accountId);
            return new ResponseEntity<>("Transactions deleted successfully", HttpStatus.OK);
        }
        else {
            throw new CustomerIDNotFoundExistsException(MessageConstant.NOT_AUTHORIZED_USER);
        }
    }

}