package com.projectX.ChargerReserv.domain.chargingStation.entity;

import com.projectX.ChargerReserv.global.basic.AbstractCodedEnumConverter;
import com.projectX.ChargerReserv.global.basic.CodedEnum;
import com.projectX.ChargerReserv.global.error.IllegalArgumentException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 전기차 충전소 기관 아이디를 나타내는 열거형(enum)입니다.
 * 각 기관은 이름과 코드를 가지고 있습니다.
 */
@Getter
@AllArgsConstructor
public enum Business implements CodedEnum<String> {
    AC("아우토크립트", "AC"),
    AH("아하", "AH"),
    AM("아마노코리아", "AM"),
    AP("애플망고", "AP"),
    BA("부안군", "BA"),
    BE("브라이트에너지파트너스", "BE"),
    BG("비긴스", "BG"),
    BK("비케이에너지", "BK"),
    BN("블루네트웍스", "BN"),
    BP("차밥스", "BP"),
    BS("보스시큐리티", "BS"),
    BT("보타리에너지", "BT"),
    CA("씨에스테크놀로지", "CA"),
    CB("참빛이브이씨", "CB"),
    CC("코콤", "CC"),
    CG("서울씨엔지", "CG"),
    CH("채움모빌리티", "CH"),
    CI("쿨사인", "CI"),
    CN("에바씨엔피", "CN"),
    CP("캐스트프로", "CP"),
    CR("크로커스", "CR"),
    CS("한국EV충전서비스센터", "CS"),
    CT("씨티카", "CT"),
    CU("씨어스", "CU"),
    CV("대영채비", "CV"),
    DE("대구공공시설관리공단", "DE"),
    DG("대구시", "DG"),
    DL("딜라이브", "DL"),
    DP("대유플러스", "DP"),
    DS("대선", "DS"),
    DY("동양이엔피", "DY"),
    E0("에너지플러스", "E0"),
    EA("에바", "EA"),
    EC("이지차저", "EC"),
    EE("이마트", "EE"),
    EG("에너지파트너즈", "EG"),
    EH("이앤에이치에너지", "EH"),
    EK("이노케이텍", "EK"),
    EL("엔라이튼", "EL"),
    EM("evmost", "EM"),
    EN("이엔", "EN"),
    EO("E1", "EO"),
    EP("이카플러그", "EP"),
    ER("이엘일렉트릭", "ER"),
    ET("이씨티", "ET"),
    EV("에버온", "EV"),
    EZ("차지인", "EZ"),
    G1("광주시", "G1"),
    G2("광주시", "G2"),
    GD("그린도트", "GD"),
    GN("지에스커넥트", "GN"),
    GP("군포시", "GP"),
    GR("그리드위즈", "GR"),
    GS("GS칼텍스", "GS"),
    HB("에이치엘비생명과학", "HB"),
    HD("현대자동차", "HD"),
    HE("한국전기차충전서비스", "HE"),
    HL("에이치엘비일렉", "HL"),
    HM("휴맥스이브이", "HM"),
    HP("해피차지", "HP"),
    HR("한국홈충전", "HR"),
    HS("홈앤서비스", "HS"),
    HW("한화솔루션", "HW"),
    HY("현대엔지니어링", "HY"),
    IC("인천국제공항", "IC"),
    IK("익산시", "IK"),
    IM("아이마켓코리아", "IM"),
    IN("신세계아이앤씨", "IN"),
    IO("아이온커뮤니케이션즈", "IO"),
    JA("이브이시스", "JA"),
    JC("제주에너지공사", "JC"),
    JD("제주도청", "JD"),
    JE("제주전기자동차서비스", "JE"),
    JH("종하아이앤씨", "JH"),
    JJ("전주시", "JJ"),
    JN("제이앤씨플랜", "JN"),
    JT("제주테크노파크", "JT"),
    JU("정읍시", "JU"),
    KA("기아자동차", "KA"),
    KC("한국컴퓨터", "KC"),
    KE("한국전기차인프라기술", "KE"),
    KG("KH에너지", "KG"),
    KH("김해시", "KH"),
    KI("기아자동차", "KI"),
    KJ("순천시", "KJ"),
    KL("클린일렉스", "KL"),
    KM("카카오모빌리티", "KM"),
    KN("한국환경공단", "KN"),
    KO("이브이파트너스", "KO"),
    KP("한국전력", "KP"),
    KR("이브이씨코리아", "KR"),
    KS("한국전기차솔루션", "KS"),
    KT("케이티", "KT"),
    KU("한국충전연합", "KU"),
    LC("롯데건설", "LC"),
    LD("롯데정보통신", "LD"),
    LH("LG유플러스(헬로비전)", "LH"),
    LI("엘에스이링크", "LI"),
    LU("LG유플러스", "LU"),
    MA("맥플러스", "MA"),
    ME("환경부", "ME"),
    MO("매니지온", "MO"),
    MT("모던텍", "MT"),
    NB("엔비플러스", "NB"),
    NE("에너넷", "NE"),
    NJ("나주시", "NJ"),
    NN("이브이네스트", "NN"),
    NT("한국전자금융", "NT"),
    NX("넥씽", "NX"),
    OB("현대오일뱅크", "OB"),
    PC("파킹클라우드", "PC"),
    PI("차지비", "PI"),
    PK("펌프킨", "PK"),
    PL("플러그링크", "PL"),
    PM("피라인모터스", "PM"),
    PS("이브이파킹서비스", "PS"),
    PW("파워큐브", "PW"),
    RE("레드이엔지", "RE"),
    S1("에스이피", "S1"),
    SA("설악에너텍", "SA"),
    SB("소프트베리", "SB"),
    SC("삼척시", "SC"),
    SD("스칼라데이터", "SD"),
    SE("서울시", "SE"),
    SF("스타코프", "SF"),
    SG("SK시그넷", "SG"),
    SH("에스에이치에너지", "SH"),
    SJ("세종시", "SJ"),
    SK("SK에너지", "SK"),
    SL("에스에스기전", "SL"),
    SM("성민기업", "SM"),
    SN("서울에너지공사", "SN"),
    SO("선광시스템", "SO"),
    SP("스마트포트테크놀로지", "SP"),
    SR("SK렌터카", "SR"),
    SS("투이스이브이씨", "SS"),
    ST("SK일렉링크", "ST"),
    SU("순천시 체육시설관리소", "SU"),
    SZ("SG생활안전", "SZ"),
    TB("태백시", "TB"),
    TD("타디스테크놀로지", "TD"),
    TE("테슬라", "TE"),
    TH("태현교통", "TH"),
    TL("티엘컴퍼니", "TL"),
    TM("티맵", "TM"),
    TR("한마음장애인복지회", "TR"),
    TS("태성콘텍", "TS"),
    TU("티비유", "TU"),
    TV("아이토브", "TV"),
    UN("유니이브이", "UN"),
    US("울산시", "US"),
    VT("볼타", "VT"),
    WB("웰바이오텍EVC", "WB"),
    YY("양양군", "YY");

    private final String name;
    private final String code;

    private static final Map<String, Business> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(Business::getCode, e -> e));

    @Override
    public String getCode() {
        return code;
    }

    /**
     * 주어진 코드에 해당하는 기관을 반환합니다.
     * 만약 코드가 유효하지 않으면 IllegalArgumentException을 던집니다.
     *
     * @param code 기관 코드
     * @return 주어진 코드에 해당하는 기관
     * @throws IllegalArgumentException 유효하지 않은 코드일 경우
     */
    public static Business fromCode(String code) {
        Business business = CODE_MAP.get(code);
        if (business == null) {
            throw new IllegalArgumentException("Invalid code: " + code);
        }
        return business;
    }

    @jakarta.persistence.Converter(autoApply = true)
    public static class Converter extends AbstractCodedEnumConverter<Business, String> {
        public Converter() {
            super(Business.class);
        }
    }
}
