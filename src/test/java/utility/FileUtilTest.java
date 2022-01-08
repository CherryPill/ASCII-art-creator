package utility;

import application.utility.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class FileUtilTest {

    @Test
    void imageListContainsGifTestEmptyList() {
        List<File> testEmptyList = new ArrayList<>();

        Assertions.assertFalse(FileUtil.imageListContainsGif(testEmptyList));
    }

    @Test
    void imageListContainsGifTestEmptyContainsGif() {
        List<File> testGifList = new ArrayList<>();
        testGifList.add(new File("file1.jpg"));
        testGifList.add(new File("file2.gif"));
        testGifList.add(new File("file3.jpg"));

        Assertions.assertTrue(FileUtil.imageListContainsGif(testGifList));

    }

    @Test
    void imageListContainsGifTestEmptyDoesNotContainGif() {
        List<File> testGifList = new ArrayList<>();
        testGifList.add(new File("file1.jpg"));
        testGifList.add(new File("file2.jpg"));
        testGifList.add(new File("file3.jpg"));

        Assertions.assertFalse(FileUtil.imageListContainsGif(testGifList));
    }

    @Test
    void imageListContainsGifTestEmptyNull() {
        Assertions.assertFalse(FileUtil.imageListContainsGif(null));
    }

    @Test
    void createFileListStringTestOneFilenamesNoFilenamesOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        Assertions.assertEquals("file1.jpg",
                FileUtil.createFileListString(testList));
    }

    @Test
    void createFileListStringTestEmptyList() {
        List<File> testList = new ArrayList<>();
        Assertions.assertEquals("",
                FileUtil.createFileListString(testList));
    }

    @Test
    void createFileListStringTestNull() {
        Assertions.assertEquals("",
                FileUtil.createFileListString(null));
    }

    @Test
    void createFileListStringTestTwoFilenamesNoFilenamesOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        testList.add(new File("file2.jpg"));
        Assertions.assertEquals("file1.jpg, file2.jpg",
                FileUtil.createFileListString(testList));
    }

    @Test
    void createFileListStringTestOneFilenameNoFilenamesOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        Assertions.assertEquals("file1.jpg",
                FileUtil.createFileListString(testList));
    }

    @Test
    void createFileListStringTestTwoFilenamesOneFilenameOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        testList.add(new File("filefile2.jpg"));

        Assertions.assertEquals("file1.jpg, fi..jpg",
                FileUtil.createFileListString(testList));
    }

    @Test
    void createFileListStringTestTwoFilenamesTwoFilenamesOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("filefile1.jpg"));
        testList.add(new File("filefile2.jpg"));

        Assertions.assertEquals("fi..jpg, fi..jpg",
                FileUtil.createFileListString(testList));
    }

    @Test
    void createFileListStringTestThreeFilenamesNoFilenamesOver10Chars() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        testList.add(new File("file2.jpg"));
        testList.add(new File("file3.jpg"));

        Assertions.assertEquals("file1.jpg, file2.jpg and 1 other files",
                FileUtil.createFileListString(testList));
    }

    @Test
    void createFileListStringTestThreeFilenamesOneFilenamesOver10CharsAtBeginning() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("filefile1.jpg"));
        testList.add(new File("file2.jpg"));
        testList.add(new File("file3.jpg"));

        Assertions.assertEquals("fi..jpg, file2.jpg and 1 other files",
                FileUtil.createFileListString(testList));
    }

    @Test
    void createFileListStringTestThreeFilenamesOneFilenamesOver10CharsAtEnd() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        testList.add(new File("file2.jpg"));
        testList.add(new File("filefile3.jpg"));

        Assertions.assertEquals("file1.jpg, file2.jpg and 1 other files",
                FileUtil.createFileListString(testList));

    }

    @Test
    void createFileListStringTestThreeFilenamesOneFilenamesOver10CharsInMiddle() {
        List<File> testList = new ArrayList<>();
        testList.add(new File("file1.jpg"));
        testList.add(new File("filefile2.jpg"));
        testList.add(new File("file3.jpg"));

        Assertions.assertEquals("file1.jpg, fi..jpg and 1 other files",
                FileUtil.createFileListString(testList));

    }

    @Test
    void omitExtensionTestNullString() {
        Assertions.assertEquals("", FileUtil.omitExtension(null));
    }

    @Test
    void omitExtensionTestEmptyString() {
        Assertions.assertEquals("", FileUtil.omitExtension(""));
    }

    @Test
    void omitExtensionTestRegularString() {
        Assertions.assertEquals("asdasd", FileUtil.omitExtension("asdasd"));
    }

    @Test
    void omitExtensionTestExtensionString() {
        Assertions.assertEquals("asdasd", FileUtil.omitExtension("asdasd.txt"));
    }

    @Test
    void omitExtensionTestExtensionMultipleDotsString() {
        Assertions.assertEquals("asd.asd", FileUtil.omitExtension("asd.asd.txt"));
    }

    @Test
    void omitExtensionTestSingleDotString() {
        Assertions.assertEquals("", FileUtil.omitExtension("."));
    }

    @Test
    void omitExtensionTestSingleCharExtensionString() {
        Assertions.assertEquals("a", FileUtil.omitExtension("a.txt"));
    }

    @Test
    void omitExtensionTestSingleCharString() {
        Assertions.assertEquals("a", FileUtil.omitExtension("a"));
    }

    @Test
    void omitExtensionTestExtensionOnlyString() {
        Assertions.assertEquals("", FileUtil.omitExtension(".jpg"));
    }
}
