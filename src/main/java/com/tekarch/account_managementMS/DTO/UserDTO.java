package com.tekarch.account_managementMS.DTO;

import com.tekarch.account_managementMS.models.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long userId;
    private String user_name;
    private String email;
    private String password_hash;
    private String phone_number;
    private Boolean two_factor_enabled = false;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String kyc_status = "pending";


 /*   public UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUser_name(user.getUser_name());
        dto.setEmail(user.getEmail());
        return dto;
    }
*/

}