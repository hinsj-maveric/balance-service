package com.maveric.balanceservice.dto;

import com.maveric.balanceservice.enums.Type;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AccountDto {
    private String _id;

    private Type type;

    private String customerId;

    private Date createdAt;

    private Date updatedAt;
}
