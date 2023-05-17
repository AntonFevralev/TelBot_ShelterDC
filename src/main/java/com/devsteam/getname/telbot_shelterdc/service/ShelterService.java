package com.devsteam.getname.telbot_shelterdc.service;


import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Сервис для работы с файлами приютов
 */
@Service
public class ShelterService {


    @Value("${kByte}")
    private int KBYTE;

    /**
     * Получает файл по имени
     * @param fileName
     * @return
     */
    public File getDataFile(String fileName) {
        return new File(Path.of("src/main/resources/"  + fileName).toUri());
    }


    public void uploadShelterFile(MultipartFile file, String fileName) throws IOException {
        Path filePath = Path.of(Path.of("src/main/resources/"+ fileName).toUri());
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, KBYTE);
                BufferedOutputStream bos = new BufferedOutputStream(os, KBYTE);
        ) {
            bis.transferTo(bos);
        } catch (IOException e) {
            throw new FileUploadException("Ошибка выгрузки файла");
        }
    }
}
