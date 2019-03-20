package cn.itcast.core.controller.upload;

import cn.itcast.core.entity.Result;
import cn.itcast.core.utils.fdfs.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String fileServer;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file) {
        try {
            //创建fastDFS工具类对象
            FastDFSClient fastDFS = new FastDFSClient("classpath:fastDFS/fdfs_client.conf");
            //上传并返回文件的路径和文件名
            String path = fastDFS.uploadFile(file.getBytes(), file.getOriginalFilename(), file.getSize());
            //上传后返回fastDFS文件服务器地址+ 上传后的文件路径和文件名
            System.out.println(fileServer + path);
            return new Result(true, fileServer + path);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "文件上传失败");
        }
    }
}
