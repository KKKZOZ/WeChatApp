package com.kkkzoz.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("class_group")
public class Group {
    
    private Long id;
    
    private String teacherId;
    
    private String studentId;

    public Group(String teacherId, String studentId) {
        this.teacherId = teacherId;
        this.studentId = studentId;
    }
}

