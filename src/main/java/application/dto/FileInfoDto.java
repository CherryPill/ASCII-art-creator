package application.dto;

import application.enums.ui.FileConversionType;
import lombok.Builder;
import lombok.Getter;

import java.io.File;
import java.util.List;

@Getter
@Builder
public class FileInfoDto {

    private List<File> inputFiles;

    private File outputDir;

    private FileConversionType fileConversionType;
}
