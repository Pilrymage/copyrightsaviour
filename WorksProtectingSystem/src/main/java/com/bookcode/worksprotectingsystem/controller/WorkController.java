package com.bookcode.worksprotectingsystem.controller;

import com.bookcode.worksprotectingsystem.entity.Works;
import com.bookcode.worksprotectingsystem.service.WorkService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
@RestController
@RequestMapping("/work")
public class WorkController {
    @Resource
    private WorkService workservice;
    @GetMapping("/test")
    public String testOutput(@RequestParam String a) {
        return "Hello World!"+a;
    }

    @PostMapping(path = "/add",consumes = {"multipart/form-data"})
    public String addWork(@RequestPart("data") Works work,@RequestPart("file") @NotNull MultipartFile file){//,
        workservice.addWork(work);
        //return "successfully";
        return workservice.uploadFile(file, work);
    }

    @GetMapping(path = "/delete")
    public boolean removeWork(@RequestParam("id") Long id) {
        return workservice.removeWorkById(id);
    }

    @PostMapping(path = "/update")
    public void updateWork(@RequestBody Works work) {
        workservice.updateWork(work);
    }

    @GetMapping(path = "/info")
    @ResponseBody
    public Works getWorkInformation(@RequestParam("id") Long id) {
        return workservice.getWorkInformationById(id);
    }

    @PostMapping(path = "/addWaterMark")
    public boolean addWaterMark(@RequestBody Works work) {
        return workservice.addWaterMark(work);
    }

    @PostMapping(path = "/addFingerPrint")
    public boolean addFingerPrint(@RequestBody Works work) {
        return workservice.addFingerPrint(work);
    }

    @PostMapping(path = "/downloadFile")
    public String downloadFile(@RequestBody Works works, HttpServletResponse resp) {
        return workservice.downWorkFile(works,resp);
    }

    @PostMapping(path = "/search")
    public List<Works> searchWorks(@RequestBody String name) {
        return workservice.searchWorksByName(name);
    }
}
