import application.utility.Utility;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UtilityTest {

    @Test
    public void imageListContainsGifTestEmptyList() {
        List<File> testEmptyList = new ArrayList<>();

        Assert.assertFalse(Utility.imageListContainsGif(testEmptyList));
    }

    @Test
    public void imageListContainsGifTestEmptyContainsGif() {
        List<File> testGifList = new ArrayList<>();
        testGifList.add(new File("file1.jpg"));
        testGifList.add(new File("file2.gif"));
        testGifList.add(new File("file3.jpg"));

        Assert.assertTrue(Utility.imageListContainsGif(testGifList));

    }

    @Test
    public void imageListContainsGifTestEmptyDoesNotContainGif() {
        List<File> testGifList = new ArrayList<>();
        testGifList.add(new File("file1.jpg"));
        testGifList.add(new File("file2.jpg"));
        testGifList.add(new File("file3.jpg"));

        Assert.assertFalse(Utility.imageListContainsGif(testGifList));
    }

    @Test
    public void imageListContainsGifTestEmptyNull() {
        Assert.assertFalse(Utility.imageListContainsGif(null));
    }
}
