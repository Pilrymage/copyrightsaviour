package com.bookcode.worksprotectingsystem;

//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
@Controller
public class FileDownloadController {
    @RequestMapping("downloadfile")
    public String file(){
        return "/downloadfile";
    }
    @RequestMapping(value = "/testDownload",method = RequestMethod.GET)
    public void testDownload(HttpServletResponse resp) throws IOException {
        String filename="test.txt";
        String pathName="D:/";
            File file = new File(pathName+filename);
            resp.setHeader("content-type","application/octet-stream");
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition","attachment;filename="+filename);
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                os =resp.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(file));
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
    }
}
