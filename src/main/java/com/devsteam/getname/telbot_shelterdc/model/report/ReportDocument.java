package com.devsteam.getname.telbot_shelterdc.model.report;

import javax.persistence.*;

@Entity
@Table(name = "report_doc")
public class ReportDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tgFileId;
    private String docName;
    @OneToOne
    private BinaryContent binaryContent;
    private String mimeType;
    private Long fileSize;


    public ReportDocument() {
    }

    public void setTgFileId(String tgFileId) {
        this.tgFileId = tgFileId;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public void setBinaryContent(BinaryContent binaryContent) {
        this.binaryContent = binaryContent;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
