package validator;

import application.validator.ValidationResultEntry;
import application.validator.Validator;
import javafx.scene.control.Alert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ValidatorTest {

    private Validator validator = new Validator();

    @Parameterized.Parameter()
    public Boolean isNoError;

    @Parameterized.Parameter(1)
    public List<File> inputFiles;

    @Parameterized.Parameter(2)
    public File outputDir;

    @Parameterized.Parameter(3)
    public String testCaseDescription;

    @Parameterized.Parameters(name = "{3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {true, List.of(new File(".")), new File("."), "testIfAllNonNull"},
                {false, null, null, "testIfAllNull"},
                {false, null, new File("."), "testIfinputFilesNull"},
                {false, List.of(new File(".")), null, "testIfOutputdirNull"},
        });
    }

    @Test
    public void doTest() {
        ValidationResultEntry validationResultEntry = validator.validate(inputFiles, outputDir);
        verify(validationResultEntry);
    }

    private void verify(ValidationResultEntry result) {
        if (isNoError) {
            assertTrue(Optional.ofNullable(
                    result.getErrorListByType()
                            .orElseGet(HashMap::new)
                            .get(Alert.AlertType.ERROR))
                    .orElseGet(ArrayList::new).isEmpty());
        } else {
            assertEquals(1, result.getErrorListByType().orElseGet(HashMap::new).size());
        }
    }
}
