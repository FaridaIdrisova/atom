import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class GameUnitTests {
    @Test
    public void getBullsAndCowsTest() {
        String keyWord = "java";
        String wordByUser = "atom";

        int expectedBulls = 0;
        int expectedCows = 1;

        int[] actualBullsAndCows = BullsAndCows.getBullsAndCows(keyWord, wordByUser);
        assertEquals(expectedBulls, actualBullsAndCows[0]);
        assertEquals(expectedCows, actualBullsAndCows[1]);
    }

    @Test
    public void getBullsAndCowsTest2() {
        String keyWord = "java";
        String wordByUser = "lava";

        int expectedBulls = 3;
        int expectedCows = 0;

        int[] actualBullsAndCows = BullsAndCows.getBullsAndCows(keyWord, wordByUser);
        assertEquals(expectedBulls, actualBullsAndCows[0]);
        assertEquals(expectedCows, actualBullsAndCows[1]);
    }

    @Test
    public void getBullsAndCowsTest3() {
        String keyWord = "java";
        String wordByUser = "java";

        int expectedBulls = 4;
        int expectedCows = 0;

        int[] actualBullsAndCows = BullsAndCows.getBullsAndCows(keyWord, wordByUser);
        assertEquals(expectedBulls, actualBullsAndCows[0]);
        assertEquals(expectedCows, actualBullsAndCows[1]);
    }

    @Test
    public void getDataFromDictionaryTest() {
        ArrayList<String> expectedDictionary = new ArrayList<String>();
        expectedDictionary.add("aahed");
        expectedDictionary.add("aahing");
        expectedDictionary.add("aahs");
        expectedDictionary.add("aalii");
        expectedDictionary.add("aaliis");

        ArrayList<String> actualDictionary = BullsAndCows.getDictionary("dictionary.txt");

        for (int i = 0; i < expectedDictionary.size(); i++) {
            assertEquals(expectedDictionary.get(i), actualDictionary.get(i));
        }
    }
}
