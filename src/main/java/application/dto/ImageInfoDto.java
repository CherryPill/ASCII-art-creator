package application.dto;

import lombok.Builder;
import lombok.Getter;

import java.awt.image.BufferedImage;

@Getter
@Builder
public class ImageInfoDto {

    private int x;

    private int y;

    private int width;

    private int height;

    private BufferedImage bufferedImage;
}
