package com.kkkzoz.service;


import com.kkkzoz.dto.DirDTO;
import com.kkkzoz.dto.FileDTO;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.ResultCode;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FileService {

    private static final String secretId = "AKIDQilW1GEvLVO0MRFm1zg6F7i25qMcbJku";
    //TODO:记得加密
    private static final String secretKey = "ua2D7l8E3cttd5GAoBG4cQxDCuLVNzKN";
    private static final String bucketName = "zhw-1312170899";
    private static final String prefix = "file/";

    private final UserService userService;
    COSCredentials cred = null;

    COSClient cosClient = null;
    //ap-chengdu
    FileService(UserService userService) {
        this.userService = userService;
        this.cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region("ap-chengdu");
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setHttpProtocol(HttpProtocol.https);
        cosClient = new COSClient(cred, clientConfig);
        getInfo();
    }

    private void getInfo() {
        List<Bucket> buckets = cosClient.listBuckets();
        for (Bucket bucketElement : buckets) {
            String bucketName = bucketElement.getName();
            String bucketLocation = bucketElement.getLocation();
            log.info("bucketName:{},bucketLocation:{}", bucketName, bucketLocation);
        }
    }

    public static File multipartToFile(MultipartFile multipart, String fileName) {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
        try {
            multipart.transferTo(convFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convFile;
    }

    public void uploadFile(String userId, int category,MultipartFile multipartFile,String fileName) {
        File file = multipartToFile(multipartFile, fileName);
        String key = prefix + userId + "/" + category + "/" + fileName;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        log.info("putObjectResult:{}", putObjectResult);


    }

    public List<DirDTO> getFileList(String userId) {
        log.info("userId:{}", userId);
        //获取全部资料，即科一到科四
        //注意这里的userId可以为教练的也可以为学生的
        String teacherId = userService.getTeacherIdByUserId(userId);
        List<DirDTO> dirDTOList = new ArrayList<>();
        dirDTOList.add(getDirDTO(teacherId, 1));
        dirDTOList.add(getDirDTO(teacherId, 2));
        dirDTOList.add(getDirDTO(teacherId, 3));
        dirDTOList.add(getDirDTO(teacherId, 4));
        return dirDTOList;
    }

    private DirDTO getDirDTO(String userId, int category) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(bucketName);
        listObjectsRequest.setPrefix(prefix + userId + "/" + category + "/");
        listObjectsRequest.setDelimiter("/");
        listObjectsRequest.setMaxKeys(1000);

        DirDTO dirDTO = new DirDTO();
        //设定文件夹的名字
        dirDTO.setDirName(String.valueOf(category));
        List<FileDTO> fileDTOList = new ArrayList<>();

        ObjectListing objectListing = null;
        do {
            try {
                objectListing = cosClient.listObjects(listObjectsRequest);
            } catch (CosServiceException e) {
                e.printStackTrace();
                return null;
            } catch (CosClientException e) {
                e.printStackTrace();
                return null;
            }
            // common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
            List<String> commonPrefixs = objectListing.getCommonPrefixes();

            // object summary表示所有列出的object列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                //预处理相关信息
                String key = cosObjectSummary.getKey();
                String fileName = key.substring(key.lastIndexOf("/") + 1);
                String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);

                fileDTOList
                        .add(new FileDTO(fileName,
                                fileType,
                                String.valueOf(cosClient.getObjectUrl(bucketName, key)),
                                key,
                                (int) cosObjectSummary.getSize()));

                String url = String.valueOf(cosClient.getObjectUrl(bucketName, key));
                log.info("url:{}",url);
                log.info("key:{}", key);
            }

            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());

        dirDTO.setFiles(fileDTOList);
        return dirDTO;
    }

    public ResponseVO deleteFile(Long userId, String fileKey) {

        try {
            cosClient.deleteObject(bucketName, fileKey);
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        }
        return new ResponseVO(ResultCode.SUCCESS);
    }
}
