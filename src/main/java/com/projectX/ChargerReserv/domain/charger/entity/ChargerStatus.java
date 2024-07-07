package com.projectX.ChargerReserv.domain.charger.entity;

import com.projectX.ChargerReserv.domain.chargingStation.entity.StatRegion;
import com.projectX.ChargerReserv.global.basic.AbstractCodedEnumConverter;
import com.projectX.ChargerReserv.global.basic.CodedEnum;
import com.projectX.ChargerReserv.global.error.IllegalArgumentException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ChargerStatus implements CodedEnum<String> {
    UNKNOWN("0", "알수없음"),
    COMMUNICATION_ERROR("1", "통신이상"),
    AVAILABLE("2", "사용가능"),
    CHARGING("3", "충전중"),
    OUT_OF_SERVICE("4", "운영중지"),
    UNDER_MAINTENANCE("5", "점검중"),
    RESERVED("6", "예약중");

    private final String code;
    private final String description;

    private static final Map<String, ChargerStatus> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(ChargerStatus::getCode, e -> e));

    @Override
    public String getCode() {
        return code;
    }

    /**
     * 주어진 코드에 해당하는 충전기 상태를 반환합니다.
     * 만약 코드가 유효하지 않으면 IllegalArgumentException을 던집니다.
     *
     * @param code 기관 코드
     * @return 주어진 코드에 해당하는 충전기 상태
     * @throws IllegalArgumentException 유효하지 않은 코드일 경우
     */
    public static ChargerStatus fromCode(String code) {
        ChargerStatus status = CODE_MAP.get(code);
        if (status == null) {
            throw new IllegalArgumentException("Unknown code: " + code);
        }
        return status;
    }

    @jakarta.persistence.Converter(autoApply = true)
    public static class Converter extends AbstractCodedEnumConverter<ChargerStatus, String> {
        public Converter() {
            super(ChargerStatus.class);
        }
    }
}
