package com.kkkzoz.domain.entity;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mistake {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Long questionId;
    
    private Integer wrongChoice;
}

