package com.kkkzoz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kkkzoz.domain.entity.File;
import org.springframework.stereotype.Component;

@Component("assetMapper")
public interface AssetMapper extends BaseMapper<File> {
}
