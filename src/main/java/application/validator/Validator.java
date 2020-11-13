package application.validator;

import application.constants.AppConstants;
import javafx.scene.control.Alert;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Validator {

    private ValidationResultEntry validationResultEntry;

    public ValidationResultEntry getValidationResultEntry() {
        return validationResultEntry;
    }

    public void setValidationResultEntry(ValidationResultEntry validationResultEntry) {
        this.validationResultEntry = validationResultEntry;
    }

    public ValidationResultEntry validate(List<File> inputFiles, File outputDir) {
        validationResultEntry = new ValidationResultEntry();
        validationResultEntry.getErrorListByType().orElseGet(HashMap::new).clear();
        if (Optional.ofNullable(inputFiles).orElseGet(ArrayList::new).isEmpty()) {
            validationResultEntry.insertOrReplaceListVal(Alert.AlertType.ERROR, AppConstants.UIConstants.Message.Error.NO_INPUT_FILE_CHOSEN);
        }
        if (Optional.ofNullable(outputDir).isEmpty()) {
            validationResultEntry.insertOrReplaceListVal(Alert.AlertType.ERROR, AppConstants.UIConstants.Message.Error.NO_DIR_CHOSEN);
        }
        validationResultEntry.setError(
                !Optional.ofNullable(validationResultEntry.getErrorListByType()
                        .orElseGet(HashMap::new)
                        .get(Alert.AlertType.ERROR)).orElseGet(ArrayList::new)
                        .isEmpty());
        return validationResultEntry;
    }
}
