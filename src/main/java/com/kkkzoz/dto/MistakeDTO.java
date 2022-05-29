package com.kkkzoz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MistakeDTO {
    private Long questionId;
    private int wrongChoice;
}
