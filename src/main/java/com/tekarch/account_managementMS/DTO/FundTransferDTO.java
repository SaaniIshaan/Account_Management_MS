package com.tekarch.account_managementMS.DTO;

import com.tekarch.account_managementMS.models.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FundTransferDTO {

    private Long transferId;
    private Account senderAccountId;
    private Account receiverAccountId;
    private BigDecimal amount;
    private String status = "pending";
    private LocalDateTime initiated_at;
    private LocalDateTime completed_at;

}
