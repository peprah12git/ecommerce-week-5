package com.smartcommerce.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private int userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;
    private Timestamp createdAt;
}
