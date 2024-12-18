package com.tekarch.account_managementMS.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FundTransferDTO {

    private Long transferId;
    private Integer senderAccountId;
    private Integer receiverAccountId;
    private BigDecimal amount;
    private String status = "pending";
    private LocalDateTime initiated_at;
    private LocalDateTime completed_at;

}
