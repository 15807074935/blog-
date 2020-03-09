package com.jxnu.blog.services;

import com.jxnu.blog.utils.FTPUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImp implements FileService{
    public String upload(MultipartFile file, String path){
        String filename = file.getOriginalFilename();
        String fileextensionname = filename.substring(filename.indexOf(".")+1);
        String uploadfilename = UUID.randomUUID().toString()+"."+fileextensionname;
        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetfile = new File(path,uploadfilename);
        try {
            file.transferTo(targetfile);
            boolean result = FTPUtil.uploadFile(Lists.newArrayList(targetfile));
            if(result){
                targetfile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return targetfile.getName();
    }
}
