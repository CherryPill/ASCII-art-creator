package utility;

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

    @Test
    public void createFileListStringTestOneFilenamesNoFilenamesOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        Assert.assertEquals("file1.jpg",
                Utility.createFileListString(testList));
    }

    @Test
    public void createFileListStringTestEmptyList() {
        List<File> testList = new ArrayList<>();
        Assert.assertEquals("",
                Utility.createFileListString(testList));
    }

    @Test
    public void createFileListStringTestNull() {
        Assert.assertEquals("",
                Utility.createFileListString(null));
    }

    @Test
    public void createFileListStringTestTwoFilenamesNoFilenamesOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        testList.add(new File("file2.jpg"));
        Assert.assertEquals("file1.jpg, file2.jpg",
                Utility.createFileListString(testList));
    }

    @Test
    public void createFileListStringTestOneFilenameNoFilenamesOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        Assert.assertEquals("file1.jpg",
                Utility.createFileListString(testList));
    }

    @Test
    public void createFileListStringTestTwoFilenamesOneFilenameOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        testList.add(new File("filefile2.jpg"));

        Assert.assertEquals("file1.jpg, fi..jpg",
                Utility.createFileListString(testList));
    }

    @Test
    public void createFileListStringTestTwoFilenamesTwoFilenamesOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("filefile1.jpg"));
        testList.add(new File("filefile2.jpg"));

        Assert.assertEquals("fi..jpg, fi..jpg",
                Utility.createFileListString(testList));
    }

    @Test
    public void createFileListStringTestThreeFilenamesNoFilenamesOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        testList.add(new File("file2.jpg"));
        testList.add(new File("file3.jpg"));

        Assert.assertEquals("file1.jpg, file2.jpg and 1 other files",
                Utility.createFileListString(testList));
    }

    @Test
    public void createFileListStringTestThreeFilenamesOneFilenamesOver10CharsAtBeginning() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("filefile1.jpg"));
        testList.add(new File("file2.jpg"));
        testList.add(new File("file3.jpg"));

        Assert.assertEquals("fi..jpg, file2.jpg and 1 other files",
                Utility.createFileListString(testList));
    }

    @Test
    public void createFileListStringTestThreeFilenamesOneFilenamesOver10CharsAtEnd() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        testList.add(new File("file2.jpg"));
        testList.add(new File("filefile3.jpg"));

        Assert.assertEquals("file1.jpg, file2.jpg and 1 other files",
                Utility.createFileListString(testList));

    }

    @Test
    public void createFileListStringTestThreeFilenamesOneFilenamesOver10CharsInMiddle() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        testList.add(new File("filefile2.jpg"));
        testList.add(new File("file3.jpg"));

        Assert.assertEquals("file1.jpg, fi..jpg and 1 other files",
                Utility.createFileListString(testList));

    }

    @Test
    public void omitExtensionTestNullString() {
        Assert.assertEquals("", Utility.omitExtension(null));
    }

    @Test
    public void omitExtensionTestEmptyString() {
        Assert.assertEquals("", Utility.omitExtension(""));
    }

    @Test
    public void omitExtensionTestRegularString() {
        Assert.assertEquals("asdasd", Utility.omitExtension("asdasd"));
    }

    @Test
    public void omitExtensionTestExtensionString() {
        Assert.assertEquals("asdasd", Utility.omitExtension("asdasd.txt"));
    }

    @Test
    public void omitExtensionTestExtensionMultipleDotsString() {
        Assert.assertEquals("asd.asd", Utility.omitExtension("asd.asd.txt"));
    }

    @Test
    public void omitExtensionTestSingleDotString() {
        Assert.assertEquals("", Utility.omitExtension("."));
    }

    @Test
    public void omitExtensionTestSingleCharExtensionString() {
        Assert.assertEquals("a", Utility.omitExtension("a.txt"));
    }

    @Test
    public void omitExtensionTestSingleCharString() {
        Assert.assertEquals("a", Utility.omitExtension("a"));
    }

    @Test
    public void omitExtensionTestExtensionOnlyString() {
        Assert.assertEquals("", Utility.omitExtension(".jpg"));
    }
}
