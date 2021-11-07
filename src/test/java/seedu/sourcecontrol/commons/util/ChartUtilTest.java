package seedu.sourcecontrol.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ChartUtilTest {

    @Test
    public void roundUpToNearestMultiple() {
        assertEquals(ChartUtil.roundUpToNearestMultiple(12.55, 5), 15);
        assertEquals(ChartUtil.roundUpToNearestMultiple(14.5, 5), 15);
        assertEquals(ChartUtil.roundUpToNearestMultiple(5.0, 5), 5);
        assertEquals(ChartUtil.roundUpToNearestMultiple(2.45, 5), 0);
        assertEquals(ChartUtil.roundUpToNearestMultiple(19.9, 5), 20);
    }

    @Test
    public void wrap() {
        assertEquals(ChartUtil.wrap(""), "");
        assertEquals(ChartUtil.wrap("short"), "short");
        assertEquals(ChartUtil.wrap("short word"), "short word");
        assertEquals(ChartUtil.wrap("short longword"), "short\nlongword");
        assertEquals(ChartUtil.wrap("longwordcannotfit"), "longwordcann\notfit");
        assertEquals(ChartUtil.wrap("short longwordcannotfit"), "short longwo\nrdcannotfit");
        assertEquals(ChartUtil.wrap("aspecialcase longwordcannotfit"), "aspecialcase\nlongwordcann\notfit");
        assertEquals(ChartUtil.wrap("short short short short short short short short short"),
                "short short\n" + "short short\n" + "short shor...");
        assertEquals(ChartUtil.wrap("super long long long long long assessment"),
                "super long\n" + "long long\n" + "long long ...");
        assertEquals(ChartUtil.wrap("short longwordcannotfit short longwordcannotfit"),
                "short longwo\n" + "rdcannotfit\n" + "short long...");
        assertEquals(ChartUtil.wrap("superlongonewordsuperlongonewordsuperlongoneword"),
                "superlongone\n" + "wordsuperlon\n" + "gonewordsu...");
    }
}
