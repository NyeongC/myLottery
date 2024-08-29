package com.ccn.mylo.scheduler;

import com.ccn.mylo.Service.LottoMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    LottoMatch lottoMatch;

    @Scheduled(cron = "*/30 * * * * *") // 30초마다 실행
    public void printMessage() {
        lottoMatch.lottoMatch();
    }
}
