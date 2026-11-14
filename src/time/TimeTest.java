package time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TimeTest {

	// ================= getTotalHours =================

	@ParameterizedTest
	@ValueSource(strings = { "05:05:05", "05:00:00", "05:59:59" })
	void testGetTotalHoursGood(String candidate) {
		assertEquals(5, Time.getTotalHours(candidate), "the hours were not calculated properly");
	}

	@ParameterizedTest
	@ValueSource(strings = { "00:00:00", "00:59:59" })
	void testGetTotalHoursBoundaryLow(String candidate) {
		assertEquals(0, Time.getTotalHours(candidate), "00 hours should come back as 0");
	}

	@ParameterizedTest
	@ValueSource(strings = { "23:00:00", "23:59:59" })
	void testGetTotalHoursBoundaryHigh(String candidate) {
		assertEquals(23, Time.getTotalHours(candidate), "23 hours should come back as 23");
	}

	@Test
	void testGetTotalHoursBadNotANumber() {
		assertThrows(NumberFormatException.class, () -> Time.getTotalHours("ab:00:00"));
	}

	@Test
	void testGetTotalHoursBadTooShort() {
		assertThrows(StringIndexOutOfBoundsException.class, () -> Time.getTotalHours("5"));
	}

	// ================= getTotalMinutes =================

	@ParameterizedTest
	@ValueSource(strings = { "05:05:05", "01:05:59", "23:05:00" })
	void testGetTotalMinutesGood(String candidate) {
		assertEquals(5, Time.getTotalMinutes(candidate), "the minutes were not calculated properly");
	}

	@ParameterizedTest
	@ValueSource(strings = { "05:00:00", "23:00:59" })
	void testGetTotalMinutesBoundaryLow(String candidate) {
		assertEquals(0, Time.getTotalMinutes(candidate), "00 minutes should come back as 0");
	}

	@ParameterizedTest
	@ValueSource(strings = { "05:59:05", "00:59:59" })
	void testGetTotalMinutesBoundaryHigh(String candidate) {
		assertEquals(59, Time.getTotalMinutes(candidate), "59 minutes should come back as 59");
	}

	@Test
	void testGetTotalMinutesBadTooLong() {
		assertThrows(NumberFormatException.class, () -> Time.getTotalMinutes("05:05:05:99"));
	}

	// a short string slips past the length check and substring(3,5) grabs "00",
	// so this returns 0 instead of throwing. documenting the real behaviour here,
	// but it looks like a defect in Time.java worth raising in class
	@Test
	void testGetTotalMinutesBadShortStringReturnsZero() {
		assertEquals(0, Time.getTotalMinutes("10:00"), "short input currently returns 0 rather than throwing");
	}

	// ================= getSeconds =================

	@ParameterizedTest
	@ValueSource(strings = { "05:05:05", "00:00:05", "23:59:05" })
	void testGetSecondsGood(String candidate) {
		assertEquals(5, Time.getSeconds(candidate), "the seconds were not calculated properly");
	}

	@ParameterizedTest
	@ValueSource(strings = { "05:05:00", "00:00:00" })
	void testGetSecondsBoundaryLow(String candidate) {
		assertEquals(0, Time.getSeconds(candidate), "00 seconds should come back as 0");
	}

	@ParameterizedTest
	@ValueSource(strings = { "05:05:59", "23:59:59" })
	void testGetSecondsBoundaryHigh(String candidate) {
		assertEquals(59, Time.getSeconds(candidate), "59 seconds should come back as 59");
	}

	@Test
	void testGetSecondsBadTooShort() {
		assertThrows(StringIndexOutOfBoundsException.class, () -> Time.getSeconds("10:00"));
	}

	@Test
	void testGetSecondsBadNotANumber() {
		assertThrows(NumberFormatException.class, () -> Time.getSeconds("05:05:ab"));
	}

	// ================= getTotalSeconds =================

	@ParameterizedTest
	@ValueSource(strings = { "05:05:05" })
	void testGetTotalSecondsGood(String candidate) {
		assertEquals(18305, Time.getTotalSeconds(candidate), "the total seconds were not calculated properly");
	}

	@ParameterizedTest
	@ValueSource(strings = { "00:00:00" })
	void testGetTotalSecondsBoundaryLow(String candidate) {
		assertEquals(0, Time.getTotalSeconds(candidate), "midnight should be 0 total seconds");
	}

	@ParameterizedTest
	@ValueSource(strings = { "23:59:59" })
	void testGetTotalSecondsBoundaryHigh(String candidate) {
		assertEquals(86399, Time.getTotalSeconds(candidate), "the last second of the day should be 86399");
	}

	@Test
	void testGetTotalSecondsBadTooShort() {
		assertThrows(StringIndexOutOfBoundsException.class, () -> Time.getTotalSeconds("10:00"));
	}

	@Test
	void testGetTotalSecondsBadNotANumber() {
		assertThrows(NumberFormatException.class, () -> Time.getTotalSeconds("ab:cd:ef"));
	}

	// ================= getMilliseconds =================

	// note this one needs the longer hh:mm:ss:ms form, it reads substring(9,11)
	@ParameterizedTest
	@ValueSource(strings = { "05:05:05:99", "00:00:00:99" })
	void testGetMillisecondsGood(String candidate) {
		assertEquals(99, Time.getMilliseconds(candidate), "the milliseconds were not calculated properly");
	}

	@ParameterizedTest
	@ValueSource(strings = { "05:05:05:00", "23:59:59:00" })
	void testGetMillisecondsBoundaryLow(String candidate) {
		assertEquals(0, Time.getMilliseconds(candidate), "00 milliseconds should come back as 0");
	}

	@Test
	void testGetMillisecondsBadTooShort() {
		assertThrows(StringIndexOutOfBoundsException.class, () -> Time.getMilliseconds("05:05:05"));
	}

	@Test
	void testGetMillisecondsBadTooLong() {
		assertThrows(NumberFormatException.class, () -> Time.getMilliseconds("05:05:05:999"));
	}

	// ================= getAllMilliseconds (ICE3, TDD) =================

	@Test
	void testGetAllMillisecondsGood() {
		assertEquals(43505005, Time.getAllMilliseconds("12:05:05:05"), "the total milliseconds were not calculated properly");
	}

	@ParameterizedTest
	@ValueSource(strings = { "00:00:00:00" })
	void testGetAllMillisecondsBoundaryLow(String candidate) {
		assertEquals(0, Time.getAllMilliseconds(candidate), "00:00:00:00 should come back as 0");
	}

	@Test
	void testGetAllMillisecondsBoundaryHigh() {
		assertEquals(86399099, Time.getAllMilliseconds("23:59:59:99"), "23:59:59:99 should come back as 86399099");
	}

	@Test
	void testGetAllMillisecondsBadTooShort() {
		assertThrows(StringIndexOutOfBoundsException.class, () -> Time.getAllMilliseconds("12:05:05"));
	}

	@Test
	void testGetAllMillisecondsBadTooLong() {
		assertThrows(NumberFormatException.class, () -> Time.getAllMilliseconds("12:05:05:055"));
	}

	@Test
	void testGetAllMillisecondsBadNotNumbers() {
		assertThrows(NumberFormatException.class, () -> Time.getAllMilliseconds("ab:cd:ef:gh"));
	}
}
