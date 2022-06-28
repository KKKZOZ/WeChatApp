package com.kkkzoz.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("asset")
public class File {

    private Long id;

    private String fileName;

    private String fileContent;
}
