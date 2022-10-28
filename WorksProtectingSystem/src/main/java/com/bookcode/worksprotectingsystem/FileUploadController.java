package com.bookcode.worksprotectingsystem;

import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
@Controller
public class FileUploadController {
    @RequestMapping("/file")
    public String file(){
        return "/file";
    }
    @RequestMapping("upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file")MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File(file.getOriginalFilename())));
                out.write(file.getBytes());
                out.flush();
                out.close();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }
            catch (IOException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }
            return "上传成功";
        }
        else {
            return "上传失败，因为文件为空";
        }
    }
}
