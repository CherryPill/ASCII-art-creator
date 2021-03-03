package utility;

import application.utility.Utility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class UtilityTest {

    @Test
    void imageListContainsGifTestEmptyList() {
        List<File> testEmptyList = new ArrayList<>();

        Assertions.assertFalse(Utility.imageListContainsGif(testEmptyList));
    }

    @Test
    void imageListContainsGifTestEmptyContainsGif() {
        List<File> testGifList = new ArrayList<>();
        testGifList.add(new File("file1.jpg"));
        testGifList.add(new File("file2.gif"));
        testGifList.add(new File("file3.jpg"));

        Assertions.assertTrue(Utility.imageListContainsGif(testGifList));

    }

    @Test
    void imageListContainsGifTestEmptyDoesNotContainGif() {
        List<File> testGifList = new ArrayList<>();
        testGifList.add(new File("file1.jpg"));
        testGifList.add(new File("file2.jpg"));
        testGifList.add(new File("file3.jpg"));

        Assertions.assertFalse(Utility.imageListContainsGif(testGifList));
    }

    @Test
    void imageListContainsGifTestEmptyNull() {
        Assertions.assertFalse(Utility.imageListContainsGif(null));
    }

    @Test
    void createFileListStringTestOneFilenamesNoFilenamesOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        Assertions.assertEquals("file1.jpg",
                Utility.createFileListString(testList));
    }

    @Test
    void createFileListStringTestEmptyList() {
        List<File> testList = new ArrayList<>();
        Assertions.assertEquals("",
                Utility.createFileListString(testList));
    }

    @Test
    void createFileListStringTestNull() {
        Assertions.assertEquals("",
                Utility.createFileListString(null));
    }

    @Test
    void createFileListStringTestTwoFilenamesNoFilenamesOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        testList.add(new File("file2.jpg"));
        Assertions.assertEquals("file1.jpg, file2.jpg",
                Utility.createFileListString(testList));
    }

    @Test
    void createFileListStringTestOneFilenameNoFilenamesOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        Assertions.assertEquals("file1.jpg",
                Utility.createFileListString(testList));
    }

    @Test
    void createFileListStringTestTwoFilenamesOneFilenameOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        testList.add(new File("filefile2.jpg"));

        Assertions.assertEquals("file1.jpg, fi..jpg",
                Utility.createFileListString(testList));
    }

    @Test
    void createFileListStringTestTwoFilenamesTwoFilenamesOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("filefile1.jpg"));
        testList.add(new File("filefile2.jpg"));

        Assertions.assertEquals("fi..jpg, fi..jpg",
                Utility.createFileListString(testList));
    }

    @Test
    void createFileListStringTestThreeFilenamesNoFilenamesOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        testList.add(new File("file2.jpg"));
        testList.add(new File("file3.jpg"));

        Assertions.assertEquals("file1.jpg, file2.jpg and 1 other files",
                Utility.createFileListString(testList));
    }

    @Test
    void createFileListStringTestThreeFilenamesOneFilenamesOver10CharsAtBeginning() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("filefile1.jpg"));
        testList.add(new File("file2.jpg"));
        testList.add(new File("file3.jpg"));

        Assertions.assertEquals("fi..jpg, file2.jpg and 1 other files",
                Utility.createFileListString(testList));
    }

    @Test
    void createFileListStringTestThreeFilenamesOneFilenamesOver10CharsAtEnd() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        testList.add(new File("file2.jpg"));
        testList.add(new File("filefile3.jpg"));

        Assertions.assertEquals("file1.jpg, file2.jpg and 1 other files",
                Utility.createFileListString(testList));

    }

    @Test
    void createFileListStringTestThreeFilenamesOneFilenamesOver10CharsInMiddle() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        testList.add(new File("filefile2.jpg"));
        testList.add(new File("file3.jpg"));

        Assertions.assertEquals("file1.jpg, fi..jpg and 1 other files",
                Utility.createFileListString(testList));

    }

    @Test
    void omitExtensionTestNullString() {
        Assertions.assertEquals("", Utility.omitExtension(null));
    }

    @Test
    void omitExtensionTestEmptyString() {
        Assertions.assertEquals("", Utility.omitExtension(""));
    }

    @Test
    void omitExtensionTestRegularString() {
        Assertions.assertEquals("asdasd", Utility.omitExtension("asdasd"));
    }

    @Test
    void omitExtensionTestExtensionString() {
        Assertions.assertEquals("asdasd", Utility.omitExtension("asdasd.txt"));
    }

    @Test
    void omitExtensionTestExtensionMultipleDotsString() {
        Assertions.assertEquals("asd.asd", Utility.omitExtension("asd.asd.txt"));
    }

    @Test
    void omitExtensionTestSingleDotString() {
        Assertions.assertEquals("", Utility.omitExtension("."));
    }

    @Test
    void omitExtensionTestSingleCharExtensionString() {
        Assertions.assertEquals("a", Utility.omitExtension("a.txt"));
    }

    @Test
    void omitExtensionTestSingleCharString() {
        Assertions.assertEquals("a", Utility.omitExtension("a"));
    }

    @Test
    void omitExtensionTestExtensionOnlyString() {
        Assertions.assertEquals("", Utility.omitExtension(".jpg"));
    }
}
