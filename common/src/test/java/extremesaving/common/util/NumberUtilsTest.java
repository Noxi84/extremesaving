package extremesaving.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberUtilsTest {

    @Test
    public void isExpense() {
        assertFalse(NumberUtils.isExpense(BigDecimal.valueOf(0)));
        assertTrue(NumberUtils.isExpense(BigDecimal.valueOf(-0.01)));
        assertTrue(NumberUtils.isExpense(BigDecimal.valueOf(-0.1)));
        assertTrue(NumberUtils.isExpense(BigDecimal.valueOf(-1)));
        assertTrue(NumberUtils.isExpense(BigDecimal.valueOf(-1.5)));
        assertTrue(NumberUtils.isExpense(BigDecimal.valueOf(-58.59)));
        assertTrue(NumberUtils.isExpense(BigDecimal.valueOf(-5858.59)));
    }

    @Test
    public void isIncome() {
        assertFalse(NumberUtils.isIncome(BigDecimal.valueOf(0)));
        assertTrue(NumberUtils.isIncome(BigDecimal.valueOf(0.01)));
        assertTrue(NumberUtils.isIncome(BigDecimal.valueOf(0.1)));
        assertTrue(NumberUtils.isIncome(BigDecimal.valueOf(1)));
        assertTrue(NumberUtils.isIncome(BigDecimal.valueOf(1.5)));
        assertTrue(NumberUtils.isIncome(BigDecimal.valueOf(58.59)));
        assertTrue(NumberUtils.isIncome(BigDecimal.valueOf(5858.59)));
    }

    @Test
    public void testFormatNumber() {
        assertEquals("123.58", NumberUtils.formatNumber(BigDecimal.valueOf(123.58)));
        assertEquals("0.89", NumberUtils.formatNumber(BigDecimal.valueOf(0.89)));
        assertEquals("1,230.12", NumberUtils.formatNumber(BigDecimal.valueOf(1230.12)));
        assertEquals("58,623.58", NumberUtils.formatNumber(BigDecimal.valueOf(58623.58)));
        assertEquals("158,623.58", NumberUtils.formatNumber(BigDecimal.valueOf(158623.58)));
        assertEquals("5,158,623.58", NumberUtils.formatNumber(BigDecimal.valueOf(5158623.58)));
    }

    @Test
    public void formatPercentage() {
        assertEquals("0%", NumberUtils.formatPercentage(BigDecimal.valueOf(0.01)));
        assertEquals("0%", NumberUtils.formatPercentage(BigDecimal.valueOf(0.1)));
        assertEquals("0%", NumberUtils.formatPercentage(BigDecimal.valueOf(0.5)));
        assertEquals("0%", NumberUtils.formatPercentage(BigDecimal.valueOf(0.9)));
        assertEquals("1%", NumberUtils.formatPercentage(BigDecimal.valueOf(1)));
        assertEquals("1%", NumberUtils.formatPercentage(BigDecimal.valueOf(1.00)));
        assertEquals("1%", NumberUtils.formatPercentage(BigDecimal.valueOf(1.01)));
        assertEquals("1%", NumberUtils.formatPercentage(BigDecimal.valueOf(1.50)));
        assertEquals("1%", NumberUtils.formatPercentage(BigDecimal.valueOf(1.75)));
        assertEquals("2%", NumberUtils.formatPercentage(BigDecimal.valueOf(2)));
        assertEquals("2%", NumberUtils.formatPercentage(BigDecimal.valueOf(2.00)));
        assertEquals("99%", NumberUtils.formatPercentage(BigDecimal.valueOf(99)));
        assertEquals("99%", NumberUtils.formatPercentage(BigDecimal.valueOf(99.5)));
        assertEquals("99%", NumberUtils.formatPercentage(BigDecimal.valueOf(99.9)));
        assertEquals("99%", NumberUtils.formatPercentage(BigDecimal.valueOf(99.99)));
        assertEquals("100%", NumberUtils.formatPercentage(BigDecimal.valueOf(100)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void formatPercentageWithNegativeAmount() {
        NumberUtils.formatPercentage(BigDecimal.valueOf(-0.01));
    }

    @Test(expected = IllegalArgumentException.class)
    public void formatPercentageWithExceedingAmount() {
        NumberUtils.formatPercentage(BigDecimal.valueOf(100.01));
    }
}
