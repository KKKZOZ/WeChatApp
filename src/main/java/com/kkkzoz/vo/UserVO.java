package com.kkkzoz.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

    private String username;
    private String name;
    private String role;
    private int category;
    private String avatarUrl;
    private int yearsOfTeaching;
    private String schoolName;
    private Long phoneNumber;
}
