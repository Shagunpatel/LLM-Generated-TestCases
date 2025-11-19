package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberUtils_GPT5Test {

    // --------- toInt / toLong / toFloat / toDouble basic behavior ---------

    @Test(timeout = 1000)
    public void testToIntWithDefaultAndInvalid() {
        assertEquals(0, NumberUtils.toInt(null));
        assertEquals(42, NumberUtils.toInt(null, 42));
        assertEquals(7, NumberUtils.toInt("7", 99));
        assertEquals(5, NumberUtils.toInt("abc", 5));
    }

    @Test(timeout = 1000)
    public void testToLongWithDefaultAndInvalid() {
        assertEquals(0L, NumberUtils.toLong(null));
        assertEquals(123L, NumberUtils.toLong(null, 123L));
        assertEquals(9L, NumberUtils.toLong("9", 0L));
        assertEquals(77L, NumberUtils.toLong("oops", 77L));
    }

    @Test(timeout = 1000)
    public void testToFloatWithDefaultAndInvalid() {
        assertEquals(0.0f, NumberUtils.toFloat(null), 0.0f);
        assertEquals(1.5f, NumberUtils.toFloat("1.5"), 0.0f);
        assertEquals(3.25f, NumberUtils.toFloat("bad", 3.25f), 0.0f);
    }

    @Test(timeout = 1000)
    public void testToDoubleWithDefaultAndInvalid() {
        assertEquals(0.0d, NumberUtils.toDouble(null), 0.0d);
        assertEquals(2.75d, NumberUtils.toDouble("2.75"), 0.0d);
        assertEquals(10.0d, NumberUtils.toDouble("nope", 10.0d), 0.0d);
    }

    @Test(timeout = 1000)
    public void testToByteAndToShortWithDefaults() {
        assertEquals((byte) 0, NumberUtils.toByte(null));
        assertEquals((byte) 12, NumberUtils.toByte("12"));
        assertEquals((byte) 5, NumberUtils.toByte("x", (byte) 5));

        assertEquals((short) 0, NumberUtils.toShort(null));
        assertEquals((short) 123, NumberUtils.toShort("123"));
        assertEquals((short) 8, NumberUtils.toShort("no", (short) 8));
    }

    // --------- createFloat / createDouble / createBigDecimal sanity ---------

    @Test(timeout = 1000)
    public void testCreateFloatAndDoubleNull() {
        assertNull(NumberUtils.createFloat(null));
        assertNull(NumberUtils.createDouble(null));
    }

    @Test(timeout = 1000)
    public void testCreateFloatAndDoubleInvalid() {
        try {
            NumberUtils.createFloat("abc");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
        try {
            NumberUtils.createDouble("xyz");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test(timeout = 1000)
    public void testCreateBigDecimalBlankRejected() {
        try {
            NumberUtils.createBigDecimal("   ");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    // --------- createInteger / createLong / createBigInteger (radix handling) ---------

    @Test(timeout = 1000)
    public void testCreateIntegerHexAndOctal() {
        assertEquals(Integer.valueOf(255), NumberUtils.createInteger("0xFF"));
        assertEquals(Integer.valueOf(63), NumberUtils.createInteger("077")); // octal
        assertEquals(Integer.valueOf(-255), NumberUtils.createInteger("-0xFF"));
    }

    @Test(timeout = 1000)
    public void testCreateLongHexAndOctal() {
        assertEquals(Long.valueOf(255L), NumberUtils.createLong("0xFF"));
        assertEquals(Long.valueOf(83L), NumberUtils.createLong("0123")); // octal 83
        assertEquals(Long.valueOf(-255L), NumberUtils.createLong("-0xFF"));
    }

    @Test(timeout = 1000)
    public void testCreateBigIntegerHexOctalAndNegative() {
        assertEquals(new java.math.BigInteger("FF", 16), NumberUtils.createBigInteger("0xFF"));
        assertEquals(new java.math.BigInteger("FF", 16), NumberUtils.createBigInteger("#FF"));
        assertEquals(new java.math.BigInteger("-77", 8), NumberUtils.createBigInteger("-077"));
    }

    // --------- createNumber coverage: hex width routing, octal, float/double precision gates ---------

    @Test(timeout = 1000)
    public void testCreateNumberHexRoutingBasedOnDigits() {
        // 8 hex digits starting with '8' -> Long (not Integer)
        Number n1 = NumberUtils.createNumber("0x80000000");
        assertTrue(n1 instanceof Long);
        assertEquals(0x80000000L, ((Long) n1).longValue());

        // >16 hex digits -> BigInteger
        Number n2 = NumberUtils.createNumber("0x10000000000000000"); // 17 hex digits
        assertTrue(n2 instanceof java.math.BigInteger);

        // <=8 hex digits and first <= '7' -> Integer
        Number n3 = NumberUtils.createNumber("0x7FFFFFFF");
        assertTrue(n3 instanceof Integer);
        assertEquals(0x7FFFFFFF, ((Integer) n3).intValue());
    }

    @Test(timeout = 1000)
    public void testCreateNumberOctalLeadingZero() {
        Number n = NumberUtils.createNumber("077");
        assertTrue(n instanceof Integer);
        assertEquals(63, n.intValue()); // 077 octal == 63 decimal
    }

    @Test(timeout = 1000)
    public void testCreateNumberPrecisionFloatVsDouble() {
        // 7 decimals -> Float
        Number nFloat = NumberUtils.createNumber("1.2345678"); // 7 digits after decimal
        assertTrue(nFloat instanceof Float);
        assertEquals(1.2345678f, ((Float) nFloat).floatValue(), 1e-7f);

        // 8 decimals -> Double
        Number nDouble = NumberUtils.createNumber("1.23456789"); // 8 digits after decimal
        assertTrue(nDouble instanceof Double);
        assertEquals(1.23456789d, ((Double) nDouble).doubleValue(), 1e-12d);
    }

    @Test(timeout = 1000)
    public void testCreateNumberTypeQualifiers() {
        assertTrue(NumberUtils.createNumber("123L") instanceof Long);
        assertTrue(NumberUtils.createNumber("1.25f") instanceof Float);
        assertTrue(NumberUtils.createNumber("1.25D") instanceof Double);
    }

    @Test(timeout = 1000)
    public void testCreateNumberInvalidAndBlank() {
        try {
            NumberUtils.createNumber("   ");
            fail("Expected NumberFormatException for blank");
        } catch (NumberFormatException e) {
            // expected
        }
        try {
            NumberUtils.createNumber("1e"); // incomplete exponent
            fail("Expected NumberFormatException for invalid exponent");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    // --------- Array min/max (including NaN behavior and validation) ---------

    @Test(timeout = 1000)
    public void testMinMaxIntArray() {
        assertEquals(1, NumberUtils.min(new int[]{3, 2, 1, 2}));
        assertEquals(3, NumberUtils.max(new int[]{3, 2, 1, 2}));
    }

    @Test(timeout = 1000)
    public void testMinMaxLongArray() {
        assertEquals(-5L, NumberUtils.min(new long[]{-1L, -5L, 10L}));
        assertEquals(10L, NumberUtils.max(new long[]{-1L, -5L, 10L}));
    }

    @Test(timeout = 1000)
    public void testMinMaxFloatArrayWithNaN() {
        assertTrue(Float.isNaN(NumberUtils.min(new float[]{1f, Float.NaN, 2f})));
        assertTrue(Float.isNaN(NumberUtils.max(new float[]{1f, Float.NaN, 2f})));
    }

    @Test(timeout = 1000)
    public void testMinMaxDoubleArrayWithNaN() {
        assertTrue(Double.isNaN(NumberUtils.min(new double[]{-1d, Double.NaN, 5d})));
        assertTrue(Double.isNaN(NumberUtils.max(new double[]{-1d, Double.NaN, 5d})));
    }

    @Test(timeout = 1000)
    public void testMinArrayValidation() {
        try {
            NumberUtils.min((int[]) null);
            fail("Expected IllegalArgumentException for null array");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
        try {
            NumberUtils.min(new int[]{});
            fail("Expected IllegalArgumentException for empty array");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    // --------- 3-arg min/max primitives ---------

    @Test(timeout = 1000)
    public void testThreeArgMinAndMaxIntegral() {
        assertEquals(-3, NumberUtils.min(1, -3, 2));
        assertEquals(9, NumberUtils.max(9, 9, 9));
        assertEquals((short) -2, NumberUtils.min((short) 5, (short) -2, (short) 0));
        assertEquals((byte) 7, NumberUtils.max((byte) 7, (byte) 6, (byte) -1));
    }

    @Test(timeout = 1000)
    public void testThreeArgMinMaxFloatingAndNaN() {
        assertEquals(1.0d, NumberUtils.min(2.0d, 3.0d, 1.0d), 0.0d);
        assertEquals(3.0f, NumberUtils.max(1.0f, 3.0f, 2.0f), 0.0f);
        assertTrue(Double.isNaN(NumberUtils.min(Double.NaN, 1.0d, 2.0d)));
        assertTrue(Float.isNaN(NumberUtils.max(Float.NaN, 1.0f, 2.0f)));
    }

    // --------- isDigits / isNumber behavior ---------

    @Test(timeout = 1000)
    public void testIsDigits() {
        assertFalse(NumberUtils.isDigits(null));
        assertFalse(NumberUtils.isDigits(""));
        assertTrue(NumberUtils.isDigits("0123456789"));
        assertFalse(NumberUtils.isDigits("12 34"));
        assertFalse(NumberUtils.isDigits("12a34"));
    }

    @Test(timeout = 1000)
    public void testIsNumberVarious() {
        assertTrue(NumberUtils.isNumber("123"));
        assertTrue(NumberUtils.isNumber("-123"));
        assertTrue(NumberUtils.isNumber("0x1A"));
        assertTrue(NumberUtils.isNumber("123."));
        assertFalse(NumberUtils.isNumber("."));
        assertTrue(NumberUtils.isNumber("1e3"));
        assertTrue(NumberUtils.isNumber("1E-3"));
        assertFalse(NumberUtils.isNumber("1e"));
        assertFalse(NumberUtils.isNumber("+123")); // leading '+' not accepted by implementation
    }
}
