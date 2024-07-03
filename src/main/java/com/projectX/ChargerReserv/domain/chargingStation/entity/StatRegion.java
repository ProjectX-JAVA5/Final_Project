package com.projectX.ChargerReserv.domain.chargingStation.entity;

import com.projectX.ChargerReserv.global.basic.AbstractCodedEnumConverter;
import com.projectX.ChargerReserv.global.basic.CodedEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 한국의 지역 구분 코드를 나타내는 열거형(enum)입니다.
 * 각 지역은 이름과 코드를 가지고 있습니다.
 */
@Getter
@AllArgsConstructor
public enum StatRegion implements CodedEnum<String> {
    SEOUL("서울특별시", "11"),
    BUSAN("부산광역시", "26"),
    DAEGU("대구광역시", "27"),
    INCHEON("인천광역시", "28"),
    GWANGJU("광주광역시", "29"),
    DAEJEON("대전광역시", "30"),
    ULSAN("울산광역시", "31"),
    SEJONG("세종특별자치시", "36"),
    GYEONGGI("경기도", "41"),
    CHUNGBUK("충청북도", "43"),
    CHUNGNAM("충청남도", "44"),
    JEONNAM("전라남도", "46"),
    GYEONGBUK("경상북도", "47"),
    GYEONGNAM("경상남도", "48"),
    JEJU("제주특별자치도", "50"),
    GANGWON("강원특별자치도", "51"),
    JEONBUK("전북특별자치도", "52"),
    ;

    private final String name;
    private final String code;

    private static final Map<String, StatRegion> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(StatRegion::getCode, e -> e));

    @Override
    public String getCode() {
        return code;
    }

    /**
     * 주어진 코드에 해당하는 지역을 반환합니다.
     * 만약 코드가 유효하지 않으면 IllegalArgumentException을 던집니다.
     *
     * @param code 지역 코드
     * @return 주어진 코드에 해당하는 지역
     * @throws IllegalArgumentException 유효하지 않은 코드일 경우
     */
    public static StatRegion fromCode(String code) {
        StatRegion region = CODE_MAP.get(code);
        if (region == null) {
            // TODO: 커스텀 예외로 변경
            throw new IllegalArgumentException("Invalid code: " + code);
        }
        return region;
    }

    @jakarta.persistence.Converter(autoApply = true)
    public static class Converter extends AbstractCodedEnumConverter<StatRegion, String> {
        public Converter() {
            super(StatRegion.class);
        }
    }
}
