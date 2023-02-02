package com.maveric.balanceservice.service;


import com.maveric.balanceservice.dto.BalanceDto;
import com.maveric.balanceservice.exception.AccountIdMismatchException;

import com.maveric.balanceservice.exception.BalanceIdNotFoundException;
import java.util.List;
public interface BalanceService {

    BalanceDto getBalanceIdByAccountId(String accountId, String balanceId) throws BalanceIdNotFoundException, AccountIdMismatchException;

    List<BalanceDto>getBalanceByAccountId(int page, int pageSize, String accountId)throws BalanceIdNotFoundException;

    String deleteBalanceByAccountId(String accountId, String balanceId) throws AccountIdMismatchException;

    Object deleteBalance(Object any, Object any1);


    public BalanceDto createBalance(String accountId, BalanceDto balanceDto);
    public BalanceDto updateBalance(String accountId,String balanceId,BalanceDto balanceDto);


    void deleteBalanceByAccountId(String accountId);
}
