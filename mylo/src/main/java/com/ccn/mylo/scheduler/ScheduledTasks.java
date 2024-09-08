package com.ccn.mylo.scheduler;

import com.ccn.mylo.Service.LottoMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@EnableAsync
public class ScheduledTasks {

    @Autowired
    LottoMatch lottoMatch;

    @Async
    @Scheduled(cron = "*/10 * * * * *") // 30초마다 실행
    public void printMessage() {
        
        System.out.println("비동기 스레드 시작");
        lottoMatch.lottoMatch();
    }
}
