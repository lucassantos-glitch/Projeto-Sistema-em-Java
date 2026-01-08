package com.farmacia.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateFlexDeserializer extends JsonDeserializer<LocalDate> {
    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        text = text.trim();
        try {
            // try ISO first
            return LocalDate.parse(text, ISO);
        } catch (DateTimeParseException e) {
            try {
                // fallback to BR format
                return LocalDate.parse(text, BR);
            } catch (DateTimeParseException ex) {
                throw new IOException("Formato de data inválido para validade: " + text + ". Use yyyy-MM-dd ou dd/MM/yyyy.");
            }
        }
    }
}
