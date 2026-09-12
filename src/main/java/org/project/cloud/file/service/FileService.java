package org.project.cloud.file.service;

import org.project.cloud.file.model.File;
import org.project.cloud.file.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private FileRepository fileRepository;
    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<File> findAll(Long userId) {
        return fileRepository.findAllByUserId(userId);
    }
}
