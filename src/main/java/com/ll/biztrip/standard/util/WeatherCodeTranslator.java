package com.ll.biztrip.standard.util;

public class WeatherCodeTranslator {

    public static String translatePTY(String pty) {
        return switch (pty) {
            case "0" -> "없음";
            case "1" -> "비";
            case "2" -> "비/눈";
            case "3" -> "눈";
            case "4" -> "소나기";
            default -> "알 수 없음";
        };
    }

    public static String translateSKY(String sky) {
        return switch (sky) {
            case "1" -> "맑음";
            case "3" -> "구름 많음";
            case "4" -> "흐림";
            default -> "알 수 없음";
        };
    }

    public static String translateCategory(String category, String value) {
        return switch (category) {
            case "PTY" -> translatePTY(value);
            case "SKY" -> translateSKY(value);
            case "TMP" -> value + "℃";
            case "POP" -> value + "%";
            case "REH" -> value + "%";
            case "WSD" -> value + "m/s";
            default -> value;
        };
    }
}
