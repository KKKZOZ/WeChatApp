package com.kkkzoz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;

    private String name;

    private String role;

    private int category;

    private String avatarUrl;

    private int yearsOfTeaching;

    private String schoolName;

    private Long phoneNumber;

    private int doneCount;

    private int numberOfDays;

    private int averageOfScores;

}
