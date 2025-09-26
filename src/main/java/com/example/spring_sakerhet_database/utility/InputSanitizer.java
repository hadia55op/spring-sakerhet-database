package com.example.spring_sakerhet_database.utility;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;

@Component
public class InputSanitizer {


    public String sanitize(String input) {
        if (input == null) return null;
        return input.replaceAll("<[^>]*>", "").trim();  // removes HTML tags
    }

}

