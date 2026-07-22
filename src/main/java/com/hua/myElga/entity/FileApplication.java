package com.hua.myElga.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "files")
public class FileApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name="pdfFile", columnDefinition="BLOB")
    private byte[] data;

    private String name;

    @OneToOne(mappedBy = "fileApplication")
    private Submission submission;

    //@OneToOne(mappedBy = "fileApplication")
    //private BreederSubmission breederSubmission;


    public FileApplication(){}
    public FileApplication(byte[] data, String name) {
        this.data = data;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public BreederSubmission getBreederSubmission() {
        //return breederSubmission;
        return null;
    }

    public void setBreederSubmission(BreederSubmission breederSubmission) {
        //this.breederSubmission = breederSubmission;
    }
}
