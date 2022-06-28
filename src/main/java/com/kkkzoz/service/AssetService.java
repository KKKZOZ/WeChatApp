package com.kkkzoz.service;

import com.kkkzoz.domain.entity.File;
import com.kkkzoz.mapper.AssetMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AssetService {

    private final AssetMapper assetMapper;



    public String getFileContent(int fileId) {
        File file = assetMapper.selectById(fileId);
        return file.getFileContent();
    }
}
