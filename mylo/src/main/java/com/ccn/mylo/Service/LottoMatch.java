package com.ccn.mylo.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LottoMatch {
    public static void main(String[] args) {
        // 미리 설정된 당첨 번호
        //int[] winningNumbers = {3, 15, 22, 28, 34, 41};
        // 최신 회차 구하기
        int drawNumber = getLottoRound();

        int[] winningNumbers = getLottoNumbers(drawNumber);

        // 당첨 번호가 나올 때까지 반복
        int attempt = 0;
        while (true) {
            attempt++;
            // 1부터 45까지의 숫자를 리스트에 추가
            List<Integer> numbers = new ArrayList<>();
            for (int i = 1; i <= 45; i++) {
                numbers.add(i);
            }

            // 숫자를 섞어서 랜덤하게 뽑기
            Collections.shuffle(numbers);

            // 6개의 숫자를 뽑아내고 정렬
            List<Integer> selectedNumbers = numbers.subList(0, 6);
            Collections.sort(selectedNumbers);

            // 뽑은 숫자를 배열로 변환
            int[] drawnNumbers = selectedNumbers.stream().mapToInt(i -> i).toArray();

            // 뽑은 숫자 출력
            // 10만 단위로 시도 횟수 출력
            if (attempt % 100000 == 0) {
                System.out.println("Attempt: " + attempt);
            }

            // 당첨 번호와 뽑은 번호가 일치하는지 확인
            if (match(winningNumbers, drawnNumbers)) {
                System.out.println("You won after " + attempt + " attempts!");

                System.out.println("Lotto numbers for draw " + drawNumber + ":");
                for (int number : winningNumbers) {
                    System.out.print(number + " ");
                }
                break;
            }
        }
    }

    // 두 배열의 숫자가 동일한지 확인하는 메서드
    public static boolean match(int[] a, int[] b) {
        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    // 로또 회차 구하는 메서도
    public static int getLottoRound(){
        // 기준 날짜와 기준 회차
        LocalDate baseDate = LocalDate.of(2002, 12, 7);
        int baseRound = 1;

        // 현재 날짜
        LocalDate currentDate = LocalDate.now();

        // 기준 날짜부터 현재 날짜까지의 주차 계산
        long weeksBetween = ChronoUnit.WEEKS.between(baseDate, currentDate);

        // 현재 회차 계산
        int currentRound = baseRound + (int) weeksBetween;
        return currentRound;
    }

    // 당첨 번호 구하는 메소드
    public static int[] getLottoNumbers(int drawNumber) {
        int[] lottoNumbers = new int[6];
        try {
            // URL 설정
            String urlString = "https://dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=" + drawNumber;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            // 응답을 읽기 위한 스트림
            InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "UTF-8");

            // JSON 파싱
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            // 로또 번호 추출
            lottoNumbers[0] = jsonObject.get("drwtNo1").getAsInt();
            lottoNumbers[1] = jsonObject.get("drwtNo2").getAsInt();
            lottoNumbers[2] = jsonObject.get("drwtNo3").getAsInt();
            lottoNumbers[3] = jsonObject.get("drwtNo4").getAsInt();
            lottoNumbers[4] = jsonObject.get("drwtNo5").getAsInt();
            lottoNumbers[5] = jsonObject.get("drwtNo6").getAsInt();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lottoNumbers;
    }

}
