package com.devsteam.getname.telbot_shelterdc.service;


import liquibase.pro.packaged.C;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
        try (InputStream in = Files.newInputStream(Path.of(fileName).toAbsolutePath());
            ){
            File targetFile = new File("ShelterTMP.json");

            java.nio.file.Files.copy(
                    in,
                    targetFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            IOUtils.closeQuietly(in);
        return targetFile;} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void uploadShelterFile(MultipartFile file, String fileName) throws IOException, URISyntaxException {
        Path filePath = Path.of(fileName).toAbsolutePath();
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (  InputStream is = file.getInputStream();

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
