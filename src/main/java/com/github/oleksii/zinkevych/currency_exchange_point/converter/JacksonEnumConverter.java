package com.github.oleksii.zinkevych.currency_exchange_point.converter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;

@Component
public class JacksonEnumConverter implements GenericConverter {

    private final ObjectMapper mapper;

    private final Set<ConvertiblePair> set;

    @Autowired
    public JacksonEnumConverter(ObjectMapper mapper) {
        set = new HashSet<>();
        set.add(new ConvertiblePair(String.class, Enum.class));
        this.mapper = mapper;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return set;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        try {
            return mapper.readValue("\"" + source + "\"", targetType.getType());
        } catch (IOException e) {
            throw new IllegalArgumentException("target: " + targetType.getName() + ", source: " + source);
        }
    }
}