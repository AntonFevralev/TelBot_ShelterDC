package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.model.report.BinaryContent;
import com.devsteam.getname.telbot_shelterdc.model.report.ReportDocument;
import com.devsteam.getname.telbot_shelterdc.repository.report.BinaryContentRepository;
import com.devsteam.getname.telbot_shelterdc.repository.report.ReportDocumentRepository;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.Message;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class ReportFileService {

    @Value("${token}")
    private String token;

    @Value("${service.file_info.uri}")
    private String fileInfoURI;
    @Value("${service.file_storage.uri}")
    private String fileStorageUri;

    private final BinaryContentRepository binaryContentRepository;

    private final ReportDocumentRepository reportDocumentRepository;

    public ReportFileService(BinaryContentRepository binaryContentRepository, ReportDocumentRepository reportDocumentRepository) {
        this.binaryContentRepository = binaryContentRepository;
        this.reportDocumentRepository = reportDocumentRepository;
    }

    public ReportDocument processDocument(Message telegramMessage) {
        String fileId = telegramMessage.document().fileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = new JSONObject(response.getBody());
            String filePath = String.valueOf(jsonObject.getJSONObject("result").getString("file_path"));
            BinaryContent transientBinaryContent = new BinaryContent();
            transientBinaryContent.setFileAsArrayOfBytes(downloadFile(filePath));
            BinaryContent persistentBinaryContent = binaryContentRepository.save(transientBinaryContent);
            Document messageDocument = telegramMessage.document();
            ReportDocument transientReportDocument = buildTransientReportDocument(messageDocument, persistentBinaryContent);
            return reportDocumentRepository.save(transientReportDocument);
        } else {
            throw new RuntimeException("Bad response from TG service" + response);
        }
    }

    private ReportDocument buildTransientReportDocument(Document messageDocument, BinaryContent persistentBinaryContent) {
        ReportDocument reportDocument = new ReportDocument();
        reportDocument.setTgFileId(messageDocument.fileId());
        reportDocument.setDocName(messageDocument.fileName());
        reportDocument.setFileSize(messageDocument.fileSize());
        reportDocument.setMimeType(messageDocument.mimeType());
        reportDocument.setBinaryContent(persistentBinaryContent);
        return reportDocument;
    }

    private byte[] downloadFile(String filePath) {
        String fullURI = fileStorageUri.replace("{token}", token)
                .replace("{filePath}", filePath);
        URL urlObj;
        try {
            urlObj = new URL(fullURI);
        } catch (MalformedURLException e) {
            throw new RuntimeException();
        }

        try (InputStream is = urlObj.openStream()){
            return is.readAllBytes();
        }catch (IOException e){
            throw new RuntimeException();
        }
    }

    private ResponseEntity<String> getFilePath(String fileId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(fileInfoURI, HttpMethod.GET, request, String.class, token, fileId);
    }
}
