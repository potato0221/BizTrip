package com.ll.biztrip.global.security.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Converter
@RequiredArgsConstructor
public class AESAttributeConverter implements AttributeConverter<String, String> {
    private final AESUtil aesUtil;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute == null ? null : aesUtil.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData == null ? null : aesUtil.decrypt(dbData);
    }
}
