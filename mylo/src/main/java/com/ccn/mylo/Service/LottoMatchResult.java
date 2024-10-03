package com.ccn.mylo.Service;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class LottoMatchResult {
    public String lottoMatch() {
        StringBuilder resultBuilder = new StringBuilder(); // StringBuilder to accumulate results

        // 미리 설정된 당첨 번호
        int drawNumber = getLottoRound();
        int[] winningNumbers = getLottoNumbers(drawNumber);

        // 당첨 번호가 나올 때까지 반복
        int attempt = 0;

        // 숫자 출현 빈도를 저장할 Map
        Map<Integer, Integer> frequencyMap = new HashMap<>();

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

            // 뽑은 숫자의 출현 빈도 증가
            for (int number : drawnNumbers) {
                frequencyMap.put(number, frequencyMap.getOrDefault(number, 0) + 1);
            }

            // 당첨 번호와 뽑은 번호가 일치하는지 확인
            if (match(winningNumbers, drawnNumbers)) {
                String formattedNumber = String.format("%,d", attempt);
                resultBuilder.append("You won after ").append(formattedNumber).append(" attempts!\n");

                resultBuilder.append("Lotto numbers for draw ").append(drawNumber).append(":\n");
                for (int number : winningNumbers) {
                    resultBuilder.append(number).append(" ");
                }
                resultBuilder.append("\n");

                int topN = 20;

                // 가장 많이 뽑힌 topN 개의 숫자 출력
                resultBuilder.append(printTopNumbers(frequencyMap, topN));

                // 원하는 개수 설정 (예: 2개의 로또 번호 추천)
                int desiredCount = 5;
                List<List<Integer>> recommendedLottoNumbers = drawRandomLottoNumbers(frequencyMap, desiredCount, topN);

                // 추천된 로또 번호 출력
                for (int i = 0; i < recommendedLottoNumbers.size(); i++) {
                    resultBuilder.append("Recommended Lotto Numbers ").append(i + 1).append(": ")
                            .append(recommendedLottoNumbers.get(i)).append("\n");
                }

                break; // 당첨되면 루프 종료
            }
        }
        return resultBuilder.toString(); // 최종 결과 문자열 반환
    }

    // 두 배열의 숫자가 동일한지 확인하는 메서드
    public boolean match(int[] a, int[] b) {
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

    // 가장 많이 뽑힌 숫자 출력 메서드
    private String printTopNumbers(Map<Integer, Integer> frequencyMap, int num) {
        StringBuilder topNumbersBuilder = new StringBuilder();
        List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(frequencyMap.entrySet());

        // 출현 횟수 기준으로 내림차순 정렬
        entryList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        topNumbersBuilder.append("Top ").append(num).append(" most drawn numbers:\n");
        for (int i = 0; i < Math.min(num, entryList.size()); i++) {
            Map.Entry<Integer, Integer> entry = entryList.get(i);
            topNumbersBuilder.append("Number: ").append(entry.getKey()).append(", Count: ").append(entry.getValue()).append("\n");
        }
        return topNumbersBuilder.toString(); // 최상위 번호 문자열 반환
    }

    // 로또 회차 구하는 메서드
    public int getLottoRound() {
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
    public int[] getLottoNumbers(int drawNumber) {
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

    public List<List<Integer>> drawRandomLottoNumbers(Map<Integer, Integer> frequencyMap, int desiredCount, int topN) {
        List<List<Integer>> result = new ArrayList<>();
        // 출현 빈도가 높은 상위 n개의 숫자 추출
        List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(frequencyMap.entrySet());
        entryList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue())); // 내림차순 정렬

        // 상위 n개 숫자 선택
        List<Integer> topNumbers = new ArrayList<>();
        for (int i = 0; i < Math.min(topN, entryList.size()); i++) {
            topNumbers.add(entryList.get(i).getKey());
        }

        Set<String> drawnNumbersSet = new HashSet<>(); // 중복 체크를 위한 집합

        // 원하는 개수만큼 로또 번호 추천
        for (int i = 0; i < desiredCount; i++) {
            List<Integer> drawnNumbers;
            do {
                Collections.shuffle(topNumbers); // 후보 리스트를 섞어서 랜덤 선택
                drawnNumbers = new ArrayList<>(topNumbers.subList(0, 6)); // 랜덤으로 6개 숫자 선택
                Collections.sort(drawnNumbers); // 선택된 숫자 정렬
            } while (drawnNumbersSet.contains(drawnNumbers.toString())); // 중복 체크

            drawnNumbersSet.add(drawnNumbers.toString()); // 선택된 숫자 저장
            result.add(drawnNumbers);
        }

        return result; // 추천된 로또 번호 리스트 반환
    }
}
