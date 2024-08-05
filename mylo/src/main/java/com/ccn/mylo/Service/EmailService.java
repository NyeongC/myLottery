package com.ccn.mylo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final MailSenderService mailSenderService;

    @Autowired
    public EmailService(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    public void sendEmail(String to, String subject, String text) {
        mailSenderService.sendEmail(to, subject, text);
    }
}