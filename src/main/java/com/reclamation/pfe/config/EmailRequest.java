package com.reclamation.pfe.config;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class EmailRequest {

    private String from;
    private List to;
    private String subject;
    private String name;

    private String data;

    private String title;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List getTo() {
        return to;
    }

    public void setTo(List to) {
        this.to = to;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
