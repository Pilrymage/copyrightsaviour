package com.bookcode.worksprotectingsystem.service.serviceImpl;


import com.bookcode.worksprotectingsystem.entity.Works;
import com.bookcode.worksprotectingsystem.service.WorkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor =  Exception.class)
public class WorkServiceImpl implements WorkService {
    @Resource
    private com.bookcode.worksprotectingsystem.Dao.WorkRepository WorkRepository;
    @Value("${websiteURL}")
    String websiteURL;
    @Override
    public void addWork(Works work) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String StrDate = formatter.format(date);
        work.setUploadtime(StrDate);            //设置上传时间
        work.setWatermarkstatus(false);        //设置水印状态
        work.setFingerprintstatus(false);      //设置指纹状态
        work.setWatermarkdate("");
        work.setFingerprintdate("");
        work.setFingerprintvalue("");
        work.setWatermarkvalue("");
        WorkRepository.save(work);
    }
    @Override
    public boolean removeWorkById(Long id) {//删除作品
        Works resu=WorkRepository.findByworkid(id);
        if (resu != null) {
            WorkRepository.deleteById(id);
            return true;
        }
        else
            return false;
    }

    @Override
    public void updateWork(Works work) {                    //更新作品
        if (work.getWorkid() != null && work.getWorkname().length() > 0) {
            Works resu=WorkRepository.findByworkid(work.getWorkid());
            if (resu != null) {
                resu.update(work);
                WorkRepository.save(resu);
            }
        }
    }

    @Override
    public Works getWorkInformationById(Long id) {          //获取作品信息通过id
        Works workResult=new Works();
        workResult=null;
        if (id > 0) {
            workResult = WorkRepository.findByworkid(id);
        }
        return workResult;
    }
    @Override
    public List<Works> searchWorksByName(String Name) {
        List<Works> res=null;
        if (!Objects.equals(Name, "")) {
            res = WorkRepository.findByworkname(Name);
        }
        return res;
    }

    @Override
    public boolean addWaterMark(Works work) {           //添加水印
        if (work.getWorkid() != null && work.getWorkname().length() > 0) {
            Works resu=WorkRepository.findByworkid(work.getWorkid());
            if (resu != null) {
                resu.setWatermarkstatus(true);
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String StrDate = formatter.format(date);
                resu.setWatermarkdate(StrDate);
                resu.setWatermarkvalue("");     //水印内容待添加
                //调用添加水印外部程序
                WorkRepository.save(resu);
                return true;
            }
            else return false;
        }
        else return false;
    }
    @Override
    public boolean addFingerPrint(Works work) {         //添加指纹
        if (work.getWorkid() != null && work.getWorkname().length() > 0) {
            Works resu=WorkRepository.findByworkid(work.getWorkid());
            if (resu != null) {
                resu.setFingerprintstatus(true);
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String StrDate = formatter.format(date);
                resu.setFingerprintdate(StrDate);
                resu.setFingerprintvalue("");   //指纹内容待添加
                //调用添加指纹外部程序
                WorkRepository.save(resu);
                return true;
            }
            else return false;
        }
        else return false;
    }
    @Override
    public String uploadFile(MultipartFile file,Works work) {
        if (!file.isEmpty()) {
            String fileName=file.getOriginalFilename();
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/";
            String sonPath = Integer.toString(Math.toIntExact(work.getOwnerid()));
            String localPath = path+sonPath+"/"+fileName;
            File dest = new File(localPath);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败"+e.getMessage();
            }
            Works oldWork = WorkRepository.findByworkid(work.getWorkid());
            oldWork.setWorkfile(localPath);
            WorkRepository.save(oldWork);
            int size= (int) file.getSize();
            return "上传成功"+Integer.toString(size)+localPath;
        }
        else {
            return "文件为空，上传失败。";
        }
    }
    @Override
    public String downWorkFile(Works work,HttpServletResponse resp) {
        int workid = Math.toIntExact(work.getWorkid());
        work = WorkRepository.findByworkid(workid);
        if (work == null)
        {
            return "文件不存在";
        }
        String downloadUrl=work.getWorkfile();
        if (!downloadUrl.isEmpty()) {
            String filename=work.getWorkname();
            String pathName=work.getWorkfile();
            File file = new File(pathName);
            if (!file.exists()) {
                return "文件不存在";
            }
            resp.reset();
            resp.setContentType("application/octet-stream");
            resp.setCharacterEncoding("utf-8");
            resp.setContentLength((int)file.length());
            resp.setHeader("Content-Disposition","attachment;filename="+filename);
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                os =resp.getOutputStream();
                bis = new BufferedInputStream(Files.newInputStream(file.toPath()));
                int i = bis.read(buff);
                while (i!=-1) {
                    os.write(buff,0,buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (bis != null) {
                    try {
                        bis.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return "下载成功";
        }
        else {
            return "下载失败，文件不存在。";
        }
    }
}
