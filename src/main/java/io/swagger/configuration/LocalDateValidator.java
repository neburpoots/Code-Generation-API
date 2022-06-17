package io.swagger.configuration;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

public class LocalDateValidator {
    private final DateTimeFormatter format;
    public LocalDateValidator(String pattern){
        this.format = DateTimeFormatter.ofPattern(pattern);
    }
    public boolean isValid(String date) {

        try {
            LocalDate.parse(date, this.format);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
