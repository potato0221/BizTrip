package com.ll.biztrip.standard.util;

public class AddressParser {

    public record ParsedAddress(String siDo, String siGunGu, String dong) {
        @Override
        public String toString() {
            return "[시도: %s, 시군구: %s]".formatted(siDo, siGunGu, dong);
        }
    }

    public static ParsedAddress parse(String fullAddress) {
        if (fullAddress == null || fullAddress.isBlank()) {
            throw new IllegalArgumentException("주소가 비어있습니다.");
        }

        String[] splitByDash = fullAddress.split(" - ");
        String actualAddress = splitByDash.length >= 2 ? splitByDash[1] : fullAddress;

        String[] parts = actualAddress.trim().split("\\s+");
        if (parts.length < 3) {
            throw new IllegalArgumentException("주소 형식이 올바르지 않습니다: " + actualAddress);
        }

        String siDo = parts[0];
        String siGunGu = parts[1];
        String dong = parts[2];

        return new ParsedAddress(siDo, siGunGu, dong);
    }
}