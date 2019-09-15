package cn.fdongl.point.file.service;

import cn.fdongl.point.file.repository.FileRepository;
import cn.fdongl.point.model.entity.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    FileRepository fileRepository;

    public Page<File> getFileList(Long userId,Pageable pageable){
        return fileRepository.findByCreateBy(userId,pageable);
    }

    public Page<File> getFileList(Long userId,String filename,Pageable pageable){
        return fileRepository.findByCreateByAndFilenameLike(userId,'%'+filename+'%',pageable);
    }

    public void save(String type,String filename, MultipartFile inputFile) throws IOException {
        String path=  ClassUtils.getDefaultClassLoader().getResource("").getPath();
        File file=new File();
        file.setType(type);
        file.setFilename(filename);
        fileRepository.save(file);
        java.io.File dest = new java.io.File(path+ java.io.File.separator + file.getId());
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        inputFile.transferTo(dest);
    }

    public void download(Long fileId, HttpServletResponse response) throws UnsupportedEncodingException {
        File file = fileRepository.getOne(fileId);
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        java.io.File file1 = new java.io.File(path + java.io.File.separator + fileId);
        if (file1.exists()) {
            response.setContentType("application/msexcel");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(file.getFilename(), "UTF-8"));
            try {
                InputStream in = new FileInputStream(file1);
                ServletOutputStream out = response.getOutputStream();
                final byte[] b = new byte[8192];
                for (int r; (r = in.read(b)) != -1; ) {
                    out.write(b, 0, r);
                }
                in.close();
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
