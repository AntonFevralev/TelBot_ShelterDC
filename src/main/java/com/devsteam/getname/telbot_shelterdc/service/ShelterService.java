package com.devsteam.getname.telbot_shelterdc.service;


import com.devsteam.getname.telbot_shelterdc.exception.NoSuchShelterException;
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

    @Value("${path.to.data.file}")
    private String dataFilePath;
    @Value("${name.of.dog.data.file}")
    private String dogShelterFileName;
    @Value("${name.of.cat.data.file}")
    private String catShelterFileName;
    @Value("${kByte}")
    private int KBYTE;

    /**
     * Сохраняет строку json в файл с данными о приюте собак
     * @param json строка с данными
     * @return
     */
    public boolean saveDogShelterToFile(String json) {
        try {
            cleanDataFile(dogShelterFileName);
            Files.writeString(Path.of(dataFilePath, dogShelterFileName), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new NoSuchShelterException();
        }
    }

    /**
     * Сохраняет строку json в файл с данными о приюте кошек
     * @param json строка с данными
     * @return
     */
    public boolean saveCatShelterToFile(String json) {
        try {
            cleanDataFile(catShelterFileName);
            Files.writeString(Path.of(dataFilePath, catShelterFileName), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new NoSuchShelterException();
        }
    }


    /**
     * очищает файл
     * @param name
     * @return
     */
    public boolean cleanDataFile(String name) {
        try {
            Path path = Path.of(dataFilePath, name);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Получает файл по имени
     * @param fileName
     * @return
     */
    public File getDataFile(String fileName) {
        return new File(dataFilePath + "/" + fileName);
    }


    public void uploadShelterSFile(MultipartFile file, String fileName) throws IOException {
        Path filePath = Path.of(dataFilePath, fileName);
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
            throw new NoSuchShelterException();
        }
    }
}
