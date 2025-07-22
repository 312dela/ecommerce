package delarosa.utils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class EmailGenerator {
    public static String generateEmail() {
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now());
        return "reg" + timestamp + "@yopmail.com";
    }

    public static String toUpperCaseFirstChar(String email) {
        return email.substring(0, 1).toUpperCase() + email.substring(1);
    }
}
