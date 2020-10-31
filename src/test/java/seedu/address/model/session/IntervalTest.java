package seedu.address.model.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

public class IntervalTest {
    private static LocalDateTime validStartTime_1 = LocalDateTime.now();
    private static LocalDateTime validStartTime_2 = LocalDateTime.of(
            2017, 2, 13, 15, 56);
    private static DateTimeFormatter SIMPLE_DATE_TIME_PATTERN_FORMATTER = DateTimeFormatter.ofPattern("EE dd MMM YYYY");


    @Test
    public void constructor_nullStartTime_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Interval(null, 1));
    }

    @Test
    public void constructor_invalidDuration_throwsIllegalArgumentException() {
        LocalDateTime validStartTime = LocalDateTime.now();
        assertThrows(IllegalArgumentException.class, () -> new Interval(validStartTime, -1));
    }

    @Test
    public void getFormattedDateTime() {
        Interval validInterval = new Interval(validStartTime_2, 60);
        assertEquals("Mon 13 Feb 2017", validInterval.getFormattedStartDateTime(SIMPLE_DATE_TIME_PATTERN_FORMATTER));
    }

    @Test
    public void getFormattedTime12hrPattern() {
        Interval validInterval = new Interval(validStartTime_2, 60);
        assertEquals("03:56 PM - 04:56 PM", validInterval.getTime12hrPattern());
    }

    @Test
    public void isPastMidnight() {
        // end is around 0100hrs on the day after start.
        Interval validInterval = new Interval(validStartTime_2, 540);
        assertTrue(validInterval.isPastMidnight());

        // end is on the same day as start, but later.
        Interval validInterval2 = new Interval(validStartTime_2, 60);
        assertFalse(validInterval2.isPastMidnight());

        // end is at 0000hrs on the day after start.
        Interval validInterval3 = new Interval(validStartTime_2, 484);
        assertTrue(validInterval3.isPastMidnight());

        // end's date of month is lower than start's date of month. Compare Feb 13 2017 and Mar 8 2017.
        Interval validInterval4 = new Interval(validStartTime_2, 33120);
        assertTrue(validInterval4.isPastMidnight());

        // end's date of month is equal to start's date of month. Compare Feb 13 2017 and Mar 12 2017.
        Interval validInterval5 = new Interval(validStartTime_2, 40320);
        assertTrue(validInterval4.isPastMidnight());

        // end is exactly 1 year after start. Compare Feb 13 2017 and Feb 12 2018.
        Interval validInterval6 = new Interval(validStartTime_2, 525600);
        assertTrue(validInterval6.isPastMidnight());
    }

    @Test
    public void getAdjustedStartDateTime() {
        // end is around 0100hrs on the day after start.
        Interval validInterval = new Interval(validStartTime_2, 540);
        assertEquals("03:56 PM - Tue 14 Feb 2017 12:56 AM", validInterval.getAdjustedStartDateTime());

        // end is on the same day as start, but later.
        Interval validInterval2 = new Interval(validStartTime_2, 60);
        assertEquals("03:56 PM - 04:56 PM", validInterval2.getAdjustedStartDateTime());

        // end is at 0000hrs on the day after start.
        Interval validInterval3 = new Interval(validStartTime_2, 484);
        assertEquals("03:56 PM - Tue 14 Feb 2017 12:00 AM", validInterval3.getAdjustedStartDateTime());

        // end is exactly 1 year after start. Compare Feb 13 2017 and Feb 12 2018.
        Interval validInterval4 = new Interval(validStartTime_2, 525600);
        assertEquals("03:56 PM - Tue 13 Feb 2018 03:56 PM", validInterval4.getAdjustedStartDateTime());
    }

    @Test
    public void isValidInterval() {
        // invalid Intervals
        assertFalse(Interval.isValidInterval(0));
        assertFalse(Interval.isValidInterval(-1));

        // valid Intervals
        assertTrue(Interval.isValidInterval(20));
    }

    @Test
    public void hashcode() {
        // same values -> returns same hashcode
        assertEquals(new Interval(validStartTime_1, 60).hashCode(),
                new Interval(validStartTime_1, 60).hashCode());

        // different startTime value -> returns different hashcode
        assertNotEquals(new Interval(validStartTime_1, 60).hashCode(),
                new Interval(validStartTime_2, 60).hashCode());

        // different duration value -> returns different hashcode
        assertNotEquals(new Interval(validStartTime_1, 60).hashCode(),
                new Interval(validStartTime_1, 40).hashCode());
    }
}

