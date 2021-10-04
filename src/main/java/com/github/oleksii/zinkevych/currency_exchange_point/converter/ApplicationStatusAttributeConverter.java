package com.github.oleksii.zinkevych.currency_exchange_point.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.github.oleksii.zinkevych.currency_exchange_point.constant.ApplicationStatus;

@Converter
public class ApplicationStatusAttributeConverter implements AttributeConverter<ApplicationStatus, String> {

    @Override
    public String convertToDatabaseColumn(ApplicationStatus applicationStatus) {
        if (applicationStatus == null) {
            return null;
        } else {
            return applicationStatus.name();
        }
    }

    @Override
    public ApplicationStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        } else {
            try {
                return ApplicationStatus.valueOf(dbData);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(dbData + " not supported.");
            }
        }
    }
}
