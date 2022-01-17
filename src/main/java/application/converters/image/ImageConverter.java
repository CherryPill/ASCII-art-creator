package application.converters.image;

import application.converters.AbstractConverter;
import application.converters.Converter;
import application.dto.InputInfoDto;
import application.encoder.GifEncoder;
import application.enums.ConversionTypeEnum;
import application.utility.GifUtility;
import application.utility.files.FileUtil;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ImageConverter extends AbstractConverter implements Converter {
    @Override
    public void convert(InputInfoDto inputInfoDto) {
        String outFileName;

        List<File> inputFiles = inputInfoDto.getFileInfoDto().getInputFiles();
        filesToProcessTotal = inputFiles.size();

        for (File file : inputFiles) {
            outFileName = FileUtil.omitExtension(file.getName()) + "_" + UUID.randomUUID() + "." + FileUtil.inferExtension(file.getName());

            //put this info to fileInfoDto in inputinfodto
            if (isGif(file)) {
                this.type = ConversionTypeEnum.IMG_GIF;
            } else {
                type = ConversionTypeEnum.IMG_OTHER;
            }
        }
        convertSingleImage(i, outFileName);
        this.filesProcessed++;
    }

    // todo check file headers maybe to unequivocally tell it's an actual gif
    private boolean isGif(File file) {
        return FileUtil.inferExtension(file.getName()).equals("gif");
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

                    if (ConversionTypeEnum.TXT.equals(type)) {
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

                        if (ConversionTypeEnum.IMG_GIF.equals(type)) {
                            if (ge == null) {
                                gifDelayTime = GifUtility.getGifDelay(reader);
                                ge = new GifEncoder(outStream, outImage.getType(), gifDelayTime);
                                ge.encode();
                            }
                            ge.write(outImage);

                        } else {
                            logger.info("Output file location: " + modifiedOutPath);
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

}
