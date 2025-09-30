package com.suhaskumar.isstracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeAll;
import java.util.TimeZone;

@SpringBootTest
class IssTrackerApplicationTests {

    @BeforeAll
    static void setup() {
        // Set default timezone to avoid JDBC / Hibernate timezone issues during tests
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
    }

    @Test
    void contextLoads() {
        /*
         * This test method is intentionally left empty.
         * It simply checks if the Spring ApplicationContext loads successfully.
         * No further assertions are required for this smoke test.
         *
         * Optionally, you could throw an UnsupportedOperationException
         * if you want to mark it as a placeholder for future implementation:
         * throw new UnsupportedOperationException("Not implemented yet")
         */
    }
}
