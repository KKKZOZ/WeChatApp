package com.kkkzoz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {

    private String fileName;
    private String fileType;
    private String fileUrl;
    private String fileKey;
    private int fileSize;
}
