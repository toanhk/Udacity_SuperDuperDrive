package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;
    private final UserService userService;

    public FileService(FileMapper fileMapper, UserService userService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
    }

    public List<File> getFiles() {
        return fileMapper.getFiles();
    }

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public boolean isFileAvailable(String filename) {
        return fileMapper.getFileByName(filename) == null;
    }

    public int uploadFile(MultipartFile fileUpload) throws IOException {
        Integer userId = userService.getCurrentUser().getUserId();

        if (isFileAvailable(fileUpload.getOriginalFilename())) {
            File file = new File(null,
                    fileUpload.getOriginalFilename(),
                    fileUpload.getContentType(),
                    String.valueOf(fileUpload.getSize()),
                    userId,
                    fileUpload.getBytes());
            return fileMapper.insert(file);
        }

        return -1;
    }

    public int deleteFile(Integer id) {
        return fileMapper.delete(id);
    }
}
