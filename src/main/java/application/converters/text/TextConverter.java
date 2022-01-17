package application.converters.text;

import application.converters.AbstractConverter;
import application.converters.Converter;
import application.dto.InputInfoDto;
import application.enums.ConversionTypeEnum;
import application.enums.ui.FileConversionType;
import application.utility.files.FileUtil;

import java.io.File;
import java.util.UUID;

public class TextConverter extends AbstractConverter implements Converter {
    @Override
    public void convert(InputInfoDto inputInfoDto) {
        String outFileName;

        filesProcessed = 1;
        filesToProcessTotal = inputFiles.size();
        for (File i : inputFiles) {
            if (FileConversionType.IMG.equals(conversionType)) {
                outFileName = FileUtil.omitExtension(i.getName()) + "_" + UUID.randomUUID() + "." + FileUtil.inferExtension(i.getName());

                //put this info to fileInfoDto in inputinfodto
                if (FileUtil.inferExtension(i.getName()).equals("gif")) {
                    this.type = ConversionTypeEnum.IMG_GIF;
                } else {
                    type = ConversionTypeEnum.IMG_OTHER;
                }
            } else {
                outFileName = FileUtil.omitExtension(i.getName()) + "_" + UUID.randomUUID() + ".txt";
                type = ConversionTypeEnum.TXT;
            }
            convertSingleImage(i, outFileName);
            this.filesProcessed++;
        }
    }
}
