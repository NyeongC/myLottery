package com.ccn.mylo.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LottoNumberFetcher {

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

    public void main(String[] args) {
        int drawNumber = 1; // 원하는 회차 번호
        int[] lottoNumbers = getLottoNumbers(drawNumber);

        System.out.println("Lotto numbers for draw " + drawNumber + ":");
        for (int number : lottoNumbers) {
            System.out.print(number + " ");
        }
    }
}
