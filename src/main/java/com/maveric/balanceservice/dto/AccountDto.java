package com.maveric.balanceservice.dto;

import com.maveric.balanceservice.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private String _id;

    private Type type;

    private String customerId;

    private Date createdAt;

    private Date updatedAt;
}
