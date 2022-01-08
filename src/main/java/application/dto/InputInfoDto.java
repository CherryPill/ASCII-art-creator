package application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InputInfoDto {

    private FileInfoDto fileInfoDto;

    private InputImageSettingsDto inputImageSettingsDto;

}
