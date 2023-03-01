package com.maveric.balanceservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.balanceservice.dto.AccountDto;
import com.maveric.balanceservice.dto.BalanceDto;
import com.maveric.balanceservice.entity.Balance;
import com.maveric.balanceservice.enums.Currency;
import com.maveric.balanceservice.enums.Type;
import com.maveric.balanceservice.feignclient.AccountFeignService;
import com.maveric.balanceservice.service.BalanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BalanceController.class)
class BalanceControllerTest {

    @MockBean
    AccountFeignService accountFeignService;

    @MockBean
    BalanceService balanceService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    private static final String API_V1_BALANCE = "/api/v1/accounts/1234/balances";

    @Test
    void getBalanceByAccountId() throws Exception{
        when(accountFeignService.getAccountByUserId("1234", "1234")).thenReturn(getAccountDtoData().getBody());
        mvc.perform(get(API_V1_BALANCE + "/1L").header("userid", "1234"))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    void shouldThrowErrorWhenGetBalanceByAccountIdByNotAuthorizedUser() throws Exception{
        when(accountFeignService.getAccountByUserId("12", "1234")).thenReturn(getAccountDtoData().getBody());
        mvc.perform(get(API_V1_BALANCE + "/1L").header("userid", "1234"))
                .andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    void shouldThrowErrorDeleteBalanceByAccountId() throws Exception{
        mvc.perform(delete(API_V1_BALANCE + "/" + "1L").header("userid", "123"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldThrowErrorForNotAuthorizedUserdeleteBalanceByAccountId() throws Exception{
        mvc.perform(delete(API_V1_BALANCE + "/" + "1L").header("userid", "1234"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldThrowErrorCreateBalance() throws Exception{
        when(accountFeignService.getAccountByUserId("1234", "1234")).thenReturn(getAccountDtoData().getBody());
        mvc.perform(post(API_V1_BALANCE).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBalanceDtoData())).header("userid", "1234"))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    public static ResponseEntity<Balance> getBalanceData(){
        Balance balance = new Balance();
        balance.set_id("1L");
        balance.setAccountId("1234");
        balance.setCurrency(Currency.INR);
        balance.setAmount(1000);
        balance.setCreatedAt(Date.from(Instant.parse("2023-02-01T00:00:00Z")));
        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }

    public static ResponseEntity<BalanceDto> getBalanceDtoData(){
        BalanceDto balance = new BalanceDto();
        balance.set_id("1L");
        balance.setAccountId("1234");
        balance.setCurrency(Currency.INR);
        balance.setAmount(1000);
        balance.setCreatedAt(Date.from(Instant.parse("2023-02-01T00:00:00Z")));
        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }

    public static ResponseEntity<AccountDto> getAccountDtoData(){
        AccountDto account = new AccountDto();
        account.set_id("1234");
        account.setType(Type.CURRENT);
        account.setCustomerId("1234");
        account.setCreatedAt(Date.from(Instant.parse("2023-02-01T00:00:00Z")));
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }
}