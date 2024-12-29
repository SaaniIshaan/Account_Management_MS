package com.tekarch.account_managementMS.DTO;

import com.tekarch.account_managementMS.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    private Long userId;
    private String username;
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