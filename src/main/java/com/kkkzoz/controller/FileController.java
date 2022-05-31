package com.kkkzoz.controller;

import com.kkkzoz.dto.DirDTO;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.ResultCode;
import com.kkkzoz.service.FileService;
import com.kkkzoz.utils.SecurityUtil;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/file")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/{fileName}")
    @ApiOperation(value = "上传文件")
    public ResponseVO uploadFile(
            @RequestPart("category") String category,
            @PathVariable("fileName") String fileName,
            @RequestPart("uploadFile") MultipartFile file) {
        String userId = SecurityUtil.getUserId();
        log.info("userId : {}", userId);
        log.info("category : {}", category);
        log.info("fileName : {}", fileName);
        log.info("fileName : {}", file.getOriginalFilename());
        log.info("fileSize : {}", file.getSize());
        log.info("fileType : {}", file.getContentType());
        fileService.uploadFile(userId, Integer.parseInt(category), file, fileName);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    @GetMapping()
    @ApiOperation(value = "获取文件列表")
    public List<DirDTO> getFileList() {
        String userId = SecurityUtil.getUserId();
        return fileService.getFileList(userId);
    }

    @DeleteMapping()
    @ApiOperation(value = "删除文件")
    public ResponseVO deleteFile(@RequestParam("userId") Long userId,
                                 @RequestParam("fileKey") String fileKey) {
        return fileService.deleteFile(userId, fileKey);
    }


}
