package com.bookcode.worksprotectingsystem.service;

import com.bookcode.worksprotectingsystem.entity.Works;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface WorkService {
    void addWork(Works work);           //新增作品
    boolean removeWorkById(Long id);    //通过id删除作品
    void updateWork(Works work);        //修改文件信息
    //Works getWorkInformationByName(String Name);    //获取
    Works getWorkInformationById(Long id);  //通过id获取作品详细信息
    List<Works> searchWorksByName(String Name);
    boolean addWaterMark(Works work);
    boolean addFingerPrint(Works work);
    String downWorkFile(Works work, HttpServletResponse resp);
    String uploadFile(@RequestParam("file")MultipartFile file,Works work);
}
