package com.kkkzoz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirDTO {

    private String dirName;
    private List<FileDTO> files;
}
