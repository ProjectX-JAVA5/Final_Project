package com.projectX.ChargerReserv.domain.charger.entity;

import com.projectX.ChargerReserv.domain.chargingStation.entity.StatRegion;
import com.projectX.ChargerReserv.global.basic.AbstractCodedEnumConverter;
import com.projectX.ChargerReserv.global.basic.CodedEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 충전기 타입을 나타내는 열거형(enum)입니다.
 * 각 타입은 이름과 코드를 가지고 있습니다.
 */
@Getter
@AllArgsConstructor
public enum ChargerType implements CodedEnum<String> {

    DC_CHADEMO("01", "DC차데모"),
    AC_SLOW("02", "AC완속"),
    DC_CHADEMO_AC_THREE_PHASE("03", "DC차데모+AC3상"),
    DC_COMBO("04", "DC콤보"),
    DC_CHADEMO_DC_COMBO("05", "DC차데모+DC콤보"),
    DC_CHADEMO_AC_THREE_PHASE_DC_COMBO("06", "DC차데모+AC3상+DC콤보"),
    AC_THREE_PHASE("07", "AC3상"),
    DC_COMBO_SLOW("08", "DC콤보(완속)"),
    H2("89", "H2");

    private final String code;
    private final String description;

    private static final Map<String, ChargerType> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(ChargerType::getCode, e -> e));

    @Override
    public String getCode() {
        return code;
    }

    /**
     * 주어진 코드에 해당하는 충전기 타입을 반환합니다.
     * 만약 코드가 유효하지 않으면 IllegalArgumentException을 던집니다.
     *
     * @param code 충전기 타입 코드
     * @return 주어진 코드에 해당하는 충전기 타입
     * @throws IllegalArgumentException 유효하지 않은 코드일 경우
     */
    public static ChargerType fromCode(String code) {
        ChargerType type = CODE_MAP.get(code);
        if (type == null) {
            // TODO: 커스텀 예외로 변경
            throw new IllegalArgumentException("Invalid code: " + code);
        }
        return type;
    }

    @jakarta.persistence.Converter(autoApply = true)
    public static class Converter extends AbstractCodedEnumConverter<ChargerType, String> {
        public Converter() {
            super(ChargerType.class);
        }
    }
}

