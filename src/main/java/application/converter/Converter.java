package application.converter;

import application.encoder.GifEncoder;
import application.utility.GifUtility;
import application.utility.Utility;
import javafx.concurrent.Task;
import javafx.stage.Stage;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Converter<V> extends Task<V> {
    private static final Logger logger = LogManager.getLogger(Converter.class);
    public Converter(List<File> inputFiles,
                     File outputDir,
                     Converter.UI_OUTFILE_CONVERSION_TYPE conversionType,
                     int blockSize,
                     Color background,
                     Color foreground, Stage progressWindow) {
        this.outputDir = outputDir;
        this.inputFiles = inputFiles;
        this.blockSize = blockSize;
        this.background = background;
        this.foreground = foreground;
        this.conversion = conversionType;
    }

    @Override
    protected V call() throws Exception {
        convertAllImages();
        return null;
    }

    public enum CONV_TYPE {TXT, IMG_OTHER, IMG_GIF}

    ;

    public enum UI_OUTFILE_CONVERSION_TYPE {TEXT, IMG}

    ;

    private final float scaleFactor = 1.5F;

    private static final String asciiChars = Utility.getProps().getProperty("logic.core.chars");

    private CONV_TYPE type = null;

    private static int gifDelayTime;

    private final int blockSize;

    private int blockCountForWidth;

    private int blockCountForHeight;

    private File outputDir;

    private Color background;

    private Color foreground;

    private int filesProcessed;

    private int filesToProcessTotal;

    private int blocksProcessed;

    private int blocksToProcessTotal;

    private List<File> inputFiles;

    Converter.UI_OUTFILE_CONVERSION_TYPE conversion;

    private void convertAllImages() {
        String outFileName;

        filesProcessed = 1;
        filesToProcessTotal = inputFiles.size();
        for (File i : inputFiles) {
            if (conversion == Converter.UI_OUTFILE_CONVERSION_TYPE.IMG) {
                outFileName = Utility.omitExtension(i.getName()) + "_" + UUID.randomUUID() + "." + Utility.inferExtension(i.getName());
                if (Utility.inferExtension(i.getName()).equals("gif")) {
                    this.type = Converter.CONV_TYPE.IMG_GIF;
                } else {
                    type = Converter.CONV_TYPE.IMG_OTHER;
                }
            } else {
                outFileName = Utility.omitExtension(i.getName()) + "_" + UUID.randomUUID() + ".txt";
                type = Converter.CONV_TYPE.TXT;
            }
            convertSingleImage(i, outFileName);
            this.filesProcessed++;
        }
    }

    //convert -> convertSingleImage
    public void convertSingleImage(File inputFile, String outFileName) {
        String format = Utility.inferExtension(inputFile.getName());
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

                    char charMatrix[][] = new char[blockCountForHeight][blockCountForWidth];
                    int colorMatrix[][] = new int[blockCountForHeight][blockCountForWidth];
                    System.out.println(blockCountForWidth + " " + blockCountForHeight);
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
                            if (this.foreground == null) {
                                avgColorForBlock = getAvgColor(startingPointX, startingPointY, endPointX, endPointY, frame);
                                colorMatrix[y - 1][x - 1] = (int) avgColorForBlock;
                            }
                            long avgGreyScaleForBlock = getAvgGS(startingPointX, startingPointY, endPointX, endPointY, frame);

                            charMatrix[y - 1][x - 1] = getCorrespondingChar((int) avgGreyScaleForBlock);
                            updateProgress(++this.blocksProcessed, this.blocksToProcessTotal);
                            updateMessage(inputFile.getName()+" ["+this.filesProcessed+" of "+this.filesToProcessTotal+"]");
                        }
                    }

                    if (type == Converter.CONV_TYPE.TXT) {
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

                        int yOffset = charSize;

                        int newImageWidth = blockCountForWidth * ((charSize));
                        int newImageHeight = blockCountForHeight * (charSize);

                        BufferedImage outImage = new BufferedImage(newImageWidth,
                                newImageHeight,
                                BufferedImage.TYPE_INT_RGB);
                        Graphics g = outImage.getGraphics();
                        g.setColor(this.background);
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
                            if (this.foreground == null) {
                                int xOffset = 0;
                                for (int z = 0; z < line.length(); z++) {

                                    g.setColor(new Color(colorMatrix[y][z]));
                                    g.drawString(String.valueOf(line.charAt(z)), xOffset, currYOffset);
                                    xOffset += 12;
                                }
                            } else {
                                g.setColor(this.foreground);

                                g.drawString(line.toString(), 0, currYOffset);

                            }
                            currYOffset += yOffset;
                            lastLine = line.toString();
                        }


                        outImage = outImage.getSubimage(0, 0, g.getFontMetrics().stringWidth(lastLine), newImageHeight);

                        if (type == Converter.CONV_TYPE.IMG_GIF) {
                            if (ge == null) {
                                gifDelayTime = GifUtility.getGifDelay(reader);
                                ge = new GifEncoder(outStream, outImage.getType(), gifDelayTime);
                                ge.encode();
                            }
                            ge.write(outImage);

                        } else {
                            logger.info("Output file location: " + modifiedOutPath.toString());
                            ImageIO.write(outImage, Utility.inferExtension(modifiedOutPath.getAbsolutePath()), modifiedOutPath);
                        }
                    }
                }
            }
        } catch (IOException e) {
            //TODO error message
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
        int reqIndex = i % asciiChars.length();
        return asciiChars.charAt(reqIndex);
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
        pixelMap = null;
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
        Integer currValue = null;
        Integer currValueKey = null;
        for (Map.Entry<Integer, Integer> entry : occursCounter.entrySet()) {
            currValue = entry.getValue();
            if (currValue > maxValue) {
                maxValue = currValue;
                currValueKey = entry.getKey();
            }
        }
        if (currValueKey != null) {
            return currValueKey.intValue();
        }
        return 0;
    }

    private static long getAvgGS(int x, int y, int w, int h, BufferedImage img) throws IOException {

        long pixelsCount = (w - x) * (h - y);
        long scaleAccumulator = 0;
        long avg = 0;

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
}
