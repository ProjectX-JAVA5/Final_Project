package com.projectX.ChargerReserv.global.util;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReservationNumberGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");
    private static final SecureRandom random = new SecureRandom();

    /**
     * 예약 번호를 생성합니다. 형식: YYMMDD + 랜덤숫자 4자리 + 체크섬 1자리
     *
     * @return 생성된 예약 번호
     */
    public static String generate() {
        String datePart = LocalDate.now().format(DATE_FORMATTER);
        String randomPart = String.format("%04d", random.nextInt(10000));
        String checkSum = calculateCheckSum(datePart + randomPart);

        return datePart + randomPart + checkSum;
    }

    /**
     * 간단한 체크섬을 계산합니다.
     * 숫자를 순회하며 모든 자리수를 더하고 마지막 숫자를 반환합니다.
     *
     * @param input 체크섬을 계산할 문자열
     * @return 계산된 체크섬 문자열
     */
    private static String calculateCheckSum(String input) {
        int sum = 0;
        for (char c : input.toCharArray()) {
            sum += Character.getNumericValue(c);
        }
        return Integer.toString(sum % 10);
    }
}
