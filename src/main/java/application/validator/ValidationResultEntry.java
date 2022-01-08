package application.validator;

import javafx.scene.control.Alert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationResultEntry {

    private final Map<Alert.AlertType, List<String>> errorListByType = new HashMap<>();

    private Boolean isError;

    public Boolean isError() {
        return isError;
    }

    public void setError(Boolean error) {
        isError = error;
    }

    public Optional<Map<Alert.AlertType, List<String>>> getErrorListByType() {
        return Optional.of(this.errorListByType);
    }

    public void insertOrReplaceListVal(Alert.AlertType at, String errorMsg) {
        getErrorListByType()
                .ifPresent(map -> Optional
                        .ofNullable(map.get(at))
                        .ifPresentOrElse(list ->
                                        map.put(at, Stream.concat(list.stream(), Stream.of(errorMsg))
                                                .collect(Collectors.toList())),
                                () -> map.put(at, List.of(errorMsg))
                        ));
    }
}
