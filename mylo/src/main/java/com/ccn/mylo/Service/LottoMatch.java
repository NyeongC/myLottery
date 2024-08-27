package com.ccn.mylo.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LottoMatch {
    public static void main(String[] args) {
        // 미리 설정된 당첨 번호
        int[] winningNumbers = {3, 15, 22, 28, 34, 41};

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
}
