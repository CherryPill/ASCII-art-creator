package application.dto;

import application.enums.ConversionAlgorithm;
import lombok.Builder;
import lombok.Getter;

import java.awt.*;

@Getter
@Builder
public class InputImageSettingsDto {

    private Integer blocksX;

    private Integer blocksY;

    private Color backgroundColor;

    private Color foreGroundColor;

    private ConversionAlgorithm conversionAlgorithm;
}
