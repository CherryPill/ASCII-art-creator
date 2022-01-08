package application.converter;

import application.constants.AppConstants;
import application.converters.Converter;
import application.dto.InputInfoDto;
import application.encoder.GifEncoder;
import application.enums.ConversionType;
import application.enums.ui.FileConversionType;
import application.utility.FileUtil;
import application.utility.GifUtility;
import javafx.concurrent.Task;
import lombok.Builder;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.*;

@Builder
@Getter
public class ConverterWorker<V extends Converter> extends Task<V> {
    private static final Logger logger = LogManager.getLogger(ConverterWorker.class);

    private final float scaleFactor = 1.5F;

    private ConversionType type;

    private static int gifDelayTime;

    private int blockSize;

    private int blockCountForWidth;

    private int blockCountForHeight;

    private File outputDir;

    private Color backgroundColor;

    private Color foregroundColor;

    private int filesProcessed;

    private int filesToProcessTotal;

    private int blocksProcessed;

    private int blocksToProcessTotal;

    private List<File> inputFiles;

    private FileConversionType conversionType;

    private V converter;

    // private Converter appropriateConverter

    private InputInfoDto inputInfoDto;

    private void convertAllImages() {
        String outFileName;

        filesProcessed = 1;
        filesToProcessTotal = inputFiles.size();
        for (File i : inputFiles) {
            if (FileConversionType.IMG.equals(conversionType)) {
                outFileName = FileUtil.omitExtension(i.getName()) + "_" + UUID.randomUUID() + "." + FileUtil.inferExtension(i.getName());

                //put this info to fileInfoDto in inputinfodto
                if (FileUtil.inferExtension(i.getName()).equals("gif")) {
                    this.type = ConversionType.IMG_GIF;
                } else {
                    type = ConversionType.IMG_OTHER;
                }
            } else {
                outFileName = FileUtil.omitExtension(i.getName()) + "_" + UUID.randomUUID() + ".txt";
                type = ConversionType.TXT;
            }
            convertSingleImage(i, outFileName);
            this.filesProcessed++;
        }
    }

    //convert -> convertSingleImage
    private void convertSingleImage(File inputFile, String outFileName) {
        String format = FileUtil.inferExtension(inputFile.getName());
        File modifiedOutPath = new File(outputDir.getAbsolutePath() + File.separator + outFileName);
        GifEncoder ge = null;
        ImageInputStream stream = null;
        ImageOutputStream outStream = null;
        try {
            int charSize = 20;
            ImageReader reader = ImageIO.getImageReadersByFormatName(format).next();
            stream = ImageIO.createImageInputStream(inputFile);
            outStream = new FileImageOutputStream(modifiedOutPath);
            reader.setInput(stream);

            int frameCount = reader.getNumImages(true);
            for (int i = 0; i < frameCount; i++) {

                BufferedImage frame = reader.read(i);
                frame = scaleImage(frame);
                if (frame != null) {
                    blockCountForWidth = (frame.getWidth() / blockSize);
                    blockCountForHeight = (frame.getHeight() / blockSize);

                    char[][] charMatrix = new char[blockCountForHeight][blockCountForWidth];
                    int[][] colorMatrix = new int[blockCountForHeight][blockCountForWidth];
                    logger.info(blockCountForWidth + " " + blockCountForHeight);
                    //get pixels of this block
                    //processing each block and computing grayscale
                    blocksToProcessTotal = blockCountForHeight * blockCountForWidth;
                    blocksProcessed = 0;

                    for (int x = 1; x <= blockCountForWidth; x++) {
                        for (int y = 1; y <= blockCountForHeight; y++) {
                            int endPointX = x * blockSize;
                            int endPointY = y * blockSize;
                            int startingPointX = endPointX - blockSize;
                            int startingPointY = endPointY - blockSize;


                            long avgColorForBlock;
                            if (this.foregroundColor == null) {
                                avgColorForBlock = getAvgColor(startingPointX, startingPointY, endPointX, endPointY, frame);
                                colorMatrix[y - 1][x - 1] = (int) avgColorForBlock;
                            }
                            long avgGreyScaleForBlock = getAvgGS(startingPointX, startingPointY, endPointX, endPointY, frame);

                            charMatrix[y - 1][x - 1] = getCorrespondingChar((int) avgGreyScaleForBlock);
                            updateProgress(++this.blocksProcessed, this.blocksToProcessTotal);
                            updateMessage(inputFile.getName() + " [" + this.filesProcessed + " of " + this.filesToProcessTotal + "]");
                        }
                    }

                    if (ConversionType.TXT.equals(type)) {
                        FileWriter fw = new FileWriter(modifiedOutPath);
                        BufferedWriter bw = new BufferedWriter(fw);
                        for (int y = 0; y < blockCountForHeight; y++) {
                            for (int x = 0; x < blockCountForWidth; x++) {
                                bw.write(charMatrix[y][x]);
                            }
                            bw.write("\r\n");
                        }
                        bw.close();
                    } else {

                        int newImageWidth = blockCountForWidth * ((charSize));
                        int newImageHeight = blockCountForHeight * (charSize);

                        BufferedImage outImage = new BufferedImage(newImageWidth,
                                newImageHeight,
                                BufferedImage.TYPE_INT_RGB);
                        Graphics g = outImage.getGraphics();
                        g.setColor(this.backgroundColor);
                        g.fillRect(0, 0, newImageWidth, newImageHeight);
                        Font f = new Font("Monospaced", Font.PLAIN, charSize);
                        g.setFont(f);
                        int currYOffset = 0;

                        String lastLine = null;


                        for (int y = 0; y < blockCountForHeight; y++) {
                            StringBuilder line = new StringBuilder();
                            for (int x = 0; x < blockCountForWidth; x++) {
                                line.append(charMatrix[y][x]);
                            }
                            if (this.foregroundColor == null) {
                                int xOffset = 0;
                                for (int z = 0; z < line.length(); z++) {

                                    g.setColor(new Color(colorMatrix[y][z]));
                                    g.drawString(String.valueOf(line.charAt(z)), xOffset, currYOffset);
                                    xOffset += 12;
                                }
                            } else {
                                g.setColor(this.foregroundColor);

                                g.drawString(line.toString(), 0, currYOffset);

                            }
                            currYOffset += charSize;
                            lastLine = line.toString();
                        }


                        outImage = outImage.getSubimage(0, 0, g.getFontMetrics()
                                .stringWidth(Optional
                                        .ofNullable(lastLine)
                                        .orElseGet(String::new)), newImageHeight);

                        if (ConversionType.IMG_GIF.equals(type)) {
                            if (ge == null) {
                                gifDelayTime = GifUtility.getGifDelay(reader);
                                ge = new GifEncoder(outStream, outImage.getType(), gifDelayTime);
                                ge.encode();
                            }
                            ge.write(outImage);

                        } else {
                            logger.info("Output file location: " + modifiedOutPath.toString());
                            ImageIO.write(outImage, FileUtil.inferExtension(modifiedOutPath.getAbsolutePath()), modifiedOutPath);
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error occurred during single image conversion", e);
        } finally {
            try {
                if (ge != null) {
                    ge.dispose();
                }
                if (stream != null) {
                    stream.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static char getCorrespondingChar(int i) {
        int reqIndex = i % AppConstants.Logic.CORE_CHARS.length();
        return AppConstants.Logic.CORE_CHARS.charAt(reqIndex);
    }


    private static int[] getPixelRGBValues(BufferedImage i, int x, int y) {
        int[] values = new int[4];
        int pixel = i.getRGB(x, y);

        values[0] = pixel >> 24 & 0xff;
        values[1] = pixel >> 16 & 0xff;
        values[2] = pixel >> 8 & 0xff;
        values[3] = pixel & 0xff;
        return values;
    }

    //for separate block
    private long getAvgColor(int x, int y, int w, int h, BufferedImage img) {
        int dominantColor;
        //fillout pixelmap
        int[][] pixelMap = new int[blockSize][blockSize];
        for (int i_x = 0; x < w; x++, i_x++) {
            for (int i_y = 0; y < h; y++, i_y++) {
                int pixel = img.getRGB(x, y);
                pixelMap[i_x][i_y] = pixel;
            }
        }
        dominantColor = findDominant(pixelMap);
        return dominantColor;
    }

    private int findDominant(int[][] map) {
        Map<Integer, Integer> occursCounter = new HashMap<>();
        Integer occurredCount = null;
        for (int x = 0; x < blockSize; x++) {
            for (int y = 0; y < blockSize; y++) {
                if (map[x][y] != 0) {
                    occurredCount = occursCounter.get(map[x][y]);
                    if (occurredCount != null) {
                        occursCounter.put(map[x][y], occurredCount + 1);
                    } else {
                        occursCounter.put(map[x][y], 1);
                        occurredCount = occursCounter.get(map[x][y]);
                    }
                }
            }
        }
        Integer maxValue = occurredCount;
        Integer currValue;
        Integer currValueKey = null;
        for (Map.Entry<Integer, Integer> entry : occursCounter.entrySet()) {
            currValue = entry.getValue();
            if (currValue > maxValue) {
                maxValue = currValue;
                currValueKey = entry.getKey();
            }
        }
        if (currValueKey != null) {
            return currValueKey;
        }
        return 0;
    }

    private static long getAvgGS(int x, int y, int w, int h, BufferedImage img) throws IOException {

        long pixelsCount = (w - x) * (h - y);
        long scaleAccumulator = 0;
        long avg;

        for (; x < w; x++) {
            for (; y < h; y++) {
                int[] argb = getPixelRGBValues(img, x, y);
                long greyPixel = (argb[1] + argb[2] + argb[3]) / 3;
                scaleAccumulator += greyPixel;
            }
        }
        avg = scaleAccumulator / pixelsCount;
        return avg;
    }

    private BufferedImage scaleImage(BufferedImage rawImage) {
        BufferedImage scaled = null;
        if (rawImage != null) {
            int scaledWidth = (int) (rawImage.getWidth() * scaleFactor);
            scaled = new BufferedImage(scaledWidth, rawImage.getHeight(), rawImage.getType());
            Graphics2D g = scaled.createGraphics();
            g.drawImage(rawImage, 0, 0, scaledWidth, rawImage.getHeight(), null);
            g.dispose();
        }
        return scaled;
    }

    @Override
    protected V call() {
        convertAllImages();
        return null;
    }


}
