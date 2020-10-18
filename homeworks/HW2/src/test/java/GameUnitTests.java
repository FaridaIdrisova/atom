import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class GameUnitTests {
    @Test
    public void GetBullsAndCowsTest() {
        String keyWord = "java";
        String wordByUser = "atom";

        int expectedBulls = 0;
        int expectedCows = 1;

        int[] actualBullsAndCows = BullsAndCows.getBullsAndCows(keyWord, wordByUser);
        assertEquals(expectedBulls, actualBullsAndCows[0]);
        assertEquals(expectedCows, actualBullsAndCows[1]);
    }

    @Test
    public void GetBullsAndCowsTest2() {
        String keyWord = "java";
        String wordByUser = "lava";

        int expectedBulls = 3;
        int expectedCows = 0;

        int[] actualBullsAndCows = BullsAndCows.getBullsAndCows(keyWord, wordByUser);
        assertEquals(expectedBulls, actualBullsAndCows[0]);
        assertEquals(expectedCows, actualBullsAndCows[1]);
    }

    @Test
    public void GetBullsAndCowsTest3() {
        String keyWord = "java";
        String wordByUser = "java";

        int expectedBulls = 4;
        int expectedCows = 0;

        int[] actualBullsAndCows = BullsAndCows.getBullsAndCows(keyWord, wordByUser);
        assertEquals(expectedBulls, actualBullsAndCows[0]);
        assertEquals(expectedCows, actualBullsAndCows[1]);
    }

    @Test
    public void GetDataFromDictionaryTest() {
        ArrayList<String> expectedDictionary = new ArrayList<String>();
        expectedDictionary.add("Hello");
        expectedDictionary.add("dear");
        expectedDictionary.add("world");
        expectedDictionary.add("elephant");
        expectedDictionary.add("kitchen");

        ArrayList<String> actualDictionary = BullsAndCows.getDictionary("src\\test\\resources\\dictionaryTest.txt");

        assertEquals(expectedDictionary, actualDictionary);
    }
}
