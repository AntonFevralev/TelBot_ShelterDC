package com.devsteam.getname.telbot_shelterdc.model.report;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "binary_content")
public class BinaryContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition="BLOB")

    private byte[] fileAsArrayOfBytes;

    public BinaryContent() {
    }

    public Long getId() {
        return id;
    }

    public byte[] getFileAsArrayOfBytes() {
        return fileAsArrayOfBytes;
    }

    public void setFileAsArrayOfBytes(byte[] fileAsArrayOfBytes) {
        this.fileAsArrayOfBytes = fileAsArrayOfBytes;
    }
}
