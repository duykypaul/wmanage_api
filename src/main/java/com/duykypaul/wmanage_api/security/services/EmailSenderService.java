package com.duykypaul.wmanage_api.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    @Transactional
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }
}
