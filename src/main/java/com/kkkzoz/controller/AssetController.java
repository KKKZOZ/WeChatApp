package com.kkkzoz.controller;


import com.kkkzoz.service.AssetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/asset")
public class AssetController {

    private final AssetService assetService;



    @GetMapping("/{fileId}")
    public String getFileContent(@PathVariable int fileId){
        return assetService.getFileContent(fileId);

    }


}
