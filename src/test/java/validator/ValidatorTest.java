package validator;

import application.validator.ValidationResultEntry;
import application.validator.Validator;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

class ValidatorTest {

    static Stream<Arguments> addDataFixture() {
        return Stream.of(
                Arguments.of(true, List.of(new File(".")), new File("."), "testIfAllNonNull"),
                Arguments.of(false, null, null, "testIfAllNull"),
                Arguments.of(false, null, new File("."), "testIfinputFilesNull"),
                Arguments.of(false, List.of(new File(".")), null, "testIfOutputdirNull")
        );
    }

    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("addDataFixture")
    void doParameterizedTest(Boolean isNoError, List<File> inputFiles, File outputDir, String testCaseDescription) {
        ValidationResultEntry validationResultEntry = Validator.validate(inputFiles, outputDir);
        verify(validationResultEntry, isNoError);
    }

    private void verify(ValidationResultEntry result, Boolean isNoError) {
        if (isNoError) {
            Assertions.assertTrue(Optional.ofNullable(
                            result.getErrorListByType()
                                    .orElseGet(HashMap::new)
                                    .get(Alert.AlertType.ERROR))
                    .orElseGet(ArrayList::new).isEmpty());
        } else {
            Assertions.assertEquals(1, result.getErrorListByType().orElseGet(HashMap::new).size());
        }
    }
}
