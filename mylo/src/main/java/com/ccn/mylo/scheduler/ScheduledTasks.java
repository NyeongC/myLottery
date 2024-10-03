package com.ccn.mylo.scheduler;

import com.ccn.mylo.Service.LottoMatch;
import com.ccn.mylo.Service.LottoMatchResult;
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
    LottoMatchResult lottoMatchResult;

    //@Async
    //@Scheduled(cron = "*/10 * * * * *") // 30초마다 실행
    public void printMessage() {
        
        System.out.println("스레드 시작");
        System.out.println(lottoMatchResult.lottoMatch());
    }
}
