package com.maveric.balanceservice.repository;

import com.maveric.balanceservice.entity.Balance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository

public interface BalanceRepository extends MongoRepository<Balance,String> {



    Page<Balance> findByAccountId(Pageable pageable, String accountId);

    Object deleteBalanceByAccountId(String s, String s1);


    Balance findByAccountId(String accountId);

    Balance deleteBalanceByAccountId(String accountId);

}