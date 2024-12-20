package blog.vanillajava.time;

// This class provides code examples related to the concepts discussed in the article.
// It demonstrates handling historical dates, time zone conversions, DST transitions, performance testing considerations, 
// and formatting best practices. These snippets are illustrative and may need refinement for production use.

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.zone.ZoneRulesException;
import java.util.Locale;
import java.util.Set;

public class TimeHandlingDemonstrations {

    // == Example 1: Historical Dates and the Missing Year Zero ==
    // While the java.time API doesn't directly model ancient calendar reforms,
    //This example shows how you might handle a historical date and acknowledges the year 1 BC scenario.
    // We also demonstrate how one might react if the date falls into a range that's not well-supported by java.time.
    public static void historicalDateExample() {
        // The Gregorian calendar does not have a year zero.
        // Java's LocalDate doesn't handle BC years well; a proleptic calendar system or an external library may be required.
        // Here, we show a known historical date in the Gregorian proleptic calendar:
        LocalDate julianAdoptionUK = LocalDate.of(1752, 9, 2);
        System.out.println("Julian last day in UK: " + julianAdoptionUK);

        // After adoption of the Gregorian calendar, 1752-09-14 was the next day in UK territories.
        LocalDate gregorianAdoptionUK = LocalDate.of(1752, 9, 14);
        System.out.println("Gregorian first day in UK: " + gregorianAdoptionUK);

        // Hypothetical scenario: You might encounter a date like "0001-01-01 BC"
        // which the standard API can't handle directly. You'll need domain-specific logic.
        // For demonstration, we'll show how you'd reject or handle it.
        String yearBc = "-0001-01-01"; // Representing 1 BC in ISO proleptic format
        try {
            LocalDate bcDate = LocalDate.parse(yearBc);
            System.out.println("Parsed BC date: " + bcDate);
        } catch (DateTimeParseException e) {
            System.out.println("Cannot parse BC date directly with LocalDate: " + yearBc);
        }
    }

    // == Example 2: Handling Unusual Time Zone Offsets ==
    // Demonstrates using ZoneId to handle offsets like 30 and 45 minutes.
    // We also show how to query available zones and handle invalid zones gracefully.
    public static void unusualOffsetsExample() {
        ZoneId indiaZone = ZoneId.of("Asia/Kolkata");   // UTC+5:30
        ZoneId nepalZone = ZoneId.of("Asia/Kathmandu"); // UTC+5:45

        ZonedDateTime indiaTime = ZonedDateTime.now(indiaZone);
        ZonedDateTime nepalTime = indiaTime.withZoneSameInstant(nepalZone);

        System.out.println("Current time in India (IST): " + indiaTime);
        System.out.println("Current time in Nepal (NPT): " + nepalTime);

        // Listing all ZoneIds that contain "London" to show how we can explore available zones:
        System.out.println("Zones containing 'London':");
        Set<String> zones = ZoneId.getAvailableZoneIds();
        zones.stream()
                .filter(z -> z.contains("London"))
                .forEach(System.out::println);

        // Handling invalid zones:
        try {
            ZoneId.of("Invalid/Zone");
        } catch (ZoneRulesException e) {
            System.out.println("Caught expected exception for invalid ZoneId: " + e.getMessage());
        }
    }

    // == Example 3: Working with Daylight Saving Time (DST) ==
    // Demonstrates detecting "gaps" or "overlaps" due to DST transitions.
    // We also show a second example with the ambiguous time scenario.
    public static void dstTransitionExample() {
        ZoneId newYork = ZoneId.of("America/New_York");

        // A known DST transition date: In 2022, clocks moved forward on March 13.
        LocalDateTime beforeTransition = LocalDateTime.of(2022, 3, 13, 1, 30);
        ZonedDateTime beforeZDT = beforeTransition.atZone(newYork);

        // Add one hour, which should skip the 2 AM to 3 AM hour if DST is in effect
        ZonedDateTime afterZDT = beforeZDT.plusHours(1);

        System.out.println("Before DST transition: " + beforeZDT);
        System.out.println("After DST transition: " + afterZDT);

        // Another scenario is a backward shift causing an ambiguous time.
        // On November 6, 2022, in New York, the clock moved back from 2 AM to 1 AM, causing duplication of times.
        LocalDateTime ambiguousTime = LocalDateTime.of(2022, 11, 6, 1, 30);
        ZonedDateTime firstOccurrence = ambiguousTime.atZone(newYork).withEarlierOffsetAtOverlap();
        ZonedDateTime secondOccurrence = ambiguousTime.atZone(newYork).withLaterOffsetAtOverlap();

        System.out.println("Ambiguous time (DST overlap): " + ambiguousTime);
        System.out.println("First occurrence: " + firstOccurrence);
        System.out.println("Second occurrence: " + secondOccurrence);
    }

    // == Example 4: Caching Formatters and Locale Variations ==
    // Demonstrates caching a formatter and also formatting with different locales.
    private static final DateTimeFormatter CACHED_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z", Locale.UK);

    public static void formatterCachingExample() {
        ZonedDateTime nowLondon = ZonedDateTime.now(ZoneId.of("Europe/London"));
        String formatted = nowLondon.format(CACHED_FORMATTER);
        System.out.println("Formatted time in London (UK Locale): " + formatted);

        // Changing the locale for formatting:
        DateTimeFormatter germanFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy HH:mm z", Locale.GERMANY);
        String germanFormat = nowLondon.format(germanFormatter);
        System.out.println("Formatted time in London (German Locale): " + germanFormat);
    }

    // == Example 5: Testing Boundary Conditions ==
    // Checking leap years, handling invalid dates, and end-of-year boundaries.
    public static void boundaryConditionsExample() {
        int year = 2024; // 2024 is a leap year
        LocalDate feb29 = LocalDate.of(year, 2, 29);
        System.out.println("Leap year (" + year + ") date: " + feb29);

        // End of the year transitions
        LocalDateTime lastMomentOfYear = LocalDateTime.of(year, 12, 31, 23, 59, 59);
        System.out.println("Last moment of year " + year + ": " + lastMomentOfYear);

        // Handling invalid dates:
        try {
            LocalDate invalidDate = LocalDate.of(2021, 2, 29); // 2021 is not a leap year
            System.out.println("This should not print: " + invalidDate);
        } catch (DateTimeException e) {
            System.out.println("Caught expected exception for invalid date: " + e.getMessage());
        }

        // Pre-check before creating a date:
        int testYear = 2021;
        int testMonth = 2;
        int testDay = 29;
        if (Year.isLeap(testYear) || testDay <= YearMonth.of(testYear, testMonth).lengthOfMonth()) {
            LocalDate safeDate = LocalDate.of(testYear, testMonth, testDay);
            System.out.println("Safely created date: " + safeDate);
        } else {
            System.out.println("Cannot create date: " + testYear + "-" + testMonth + "-" + testDay + " is invalid.");
        }
    }

    // == Example 6: Encouraging Critical Thinking ==
    // Encourage writing tests around tricky dates, conversions, or historical transitions.
    public static void criticalThinkingExample() {
        // If your application processes user input dates, what if the user enters a non-standard format?
        String userInput = "2024/02/29 15:00 Europe/London"; // A date and time in a non-ISO format
        // Let's parse this carefully:
        try {
            // Suppose a known format:
            DateTimeFormatter userFmt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm VV", Locale.UK);
            ZonedDateTime parsed = ZonedDateTime.parse(userInput, userFmt);
            System.out.println("Parsed user input date: " + parsed);
        } catch (DateTimeParseException e) {
            System.out.println("Failed to parse user input: " + e.getMessage());
        }

        // Consider ambiguous times, historical data, and DST transitions.
        // E.g., what happens if the user provides a time in a zone that no longer observes DST?
        // Or if they give a historically significant date?
        // Add checks or fallbacks to ensure correctness.
    }

    public static void main(String[] args) {
        System.out.println("== Historical Date Example ==");
        historicalDateExample();
        System.out.println();

        System.out.println("== Unusual Offsets Example ==");
        unusualOffsetsExample();
        System.out.println();

        System.out.println("== DST Transition Example ==");
        dstTransitionExample();
        System.out.println();

        System.out.println("== Formatter Caching Example ==");
        formatterCachingExample();
        System.out.println();

        System.out.println("== Boundary Conditions Example ==");
        boundaryConditionsExample();
        System.out.println();

        System.out.println("== Critical Thinking Example ==");
        criticalThinkingExample();
        System.out.println();

        System.out.println("== Performance Timing Example ==");
        System.out.println();
    }
}
