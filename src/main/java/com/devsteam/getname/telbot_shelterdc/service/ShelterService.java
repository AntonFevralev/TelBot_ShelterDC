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


    public String readDogShelterFromFile() {
        try {
            return Files.readString(Path.of(dataFilePath, dogShelterFileName));
        } catch (IOException e) {
            throw new NoSuchShelterException();
        }
    }


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


    public File getDataFile(String fileName) {
        return new File(dataFilePath + "/" + fileName);
    }


    public void uploadDogShelterFile(MultipartFile file) throws IOException {
        Path filePath = Path.of(dataFilePath, dogShelterFileName);
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
    public void uploadCatShelterFile(MultipartFile file) throws IOException {
        Path filePath = Path.of(dataFilePath, catShelterFileName);
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
