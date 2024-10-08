package com.ccn.mylo.scheduler;

import com.ccn.mylo.Service.EmailService;
import com.ccn.mylo.Service.LottoMatch;
import com.ccn.mylo.Service.LottoMatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class LottoSchedule {

    private final EmailService emailService;

    @Autowired
    LottoMatchResult lottoMatch;

    @Autowired
    public LottoSchedule(EmailService emailService) {
        this.emailService = emailService;
    }


    @Scheduled(cron = "0 * * * * *") // 1분마다 실행
    public void printMessage() {
        
        System.out.println("비동기 스레드 시작");
        String to = "cjfsud23@naver.com";
        String subject = "test";
        String text = "";

        text = lottoMatch.lottoMatch();

        emailService.sendEmail(to,subject,text);

    }
}
