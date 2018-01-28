package application.converter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Converter {
	
	private final float scaleFactor = 1.5F;
	private static final String asciiChars = "-+^=>?$&@#";
	private final BufferedImage inputImage;
	
	private final int blockSize;
	
	private int blockCountForWidth;
	
	private int blockCountForHeight;
	
	private String outFile;
	
	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}
	public void writeImage(char arr[][], File out) throws IOException{
		File modifiedOutPath = new File(out.getAbsolutePath()+"\\"+outFile);
		FileWriter fw = new FileWriter(modifiedOutPath);
		BufferedWriter bw = new BufferedWriter(fw); 
		for(int y = 0;y<blockCountForHeight;y++){
			for(int x = 0;x<blockCountForWidth;x++){
				bw.write(arr[y][x]);
			}
			bw.write("\r\n");
		}
		bw.close();
	}
	
	public Converter(BufferedImage img, int blocks){
		inputImage = scaleImage(img);
		blockSize = blocks;
	}
	
	public char[][] convertToASCII() throws IOException{
		
		
		blockCountForWidth = (inputImage.getWidth()/blockSize);
		blockCountForHeight = (inputImage.getHeight()/blockSize);
		
		char charMatrix[][] = new char[blockCountForHeight][blockCountForWidth];
		
		System.out.println(blockCountForWidth+" "+blockCountForHeight);
		//get pixels of this block
		//processing each block and computing grayscale
		for(int x = 1;x<=blockCountForWidth;x++){
			for(int y = 1;y<=blockCountForHeight;y++){		
				int endPointX = x*blockSize;
				int endPointY = y*blockSize;
				int startingPointX = endPointX - blockSize;
				int startingPointY = endPointY - blockSize;
				
			
				int avgGreyScaleForBlock = getAvgGS(startingPointX, startingPointY, endPointX, endPointY, inputImage);
				
				charMatrix[y-1][x-1] = getCorrespondingChar(avgGreyScaleForBlock);
			}
		}
		return charMatrix;
	}

	private static char getCorrespondingChar(int i){
		int reqIndex = i % asciiChars.length();
		return asciiChars.charAt(reqIndex);
	}
	
	private static int getAvgGS(int x, int y, int w, int h, BufferedImage img) throws IOException{
		
		int pixelsCount = (w-x) * (h-y);
		int scaleAccumulator = 0;
		int avg = 0;
		
		for(;x<w;x++){
			for(;y<h;y++){
				int pixel = img.getRGB(x, y);
				int a = pixel >> 24 & 0xff;
			    int r = pixel >> 16 & 0xff;
				int g = pixel >> 8 & 0xff;
				int b = pixel & 0xff;
				int greyPixel = (r+g+b)/3;
				scaleAccumulator+=greyPixel;
			}
		}
		avg = scaleAccumulator / pixelsCount;
		return avg;
	}
	private BufferedImage scaleImage(BufferedImage rawImage){
		BufferedImage scaled = null;
		if(rawImage != null){
			int scaledWidth = (int) (rawImage.getWidth()*scaleFactor);
			scaled = new BufferedImage(scaledWidth, rawImage.getHeight(), rawImage.getType());
			Graphics2D g = scaled.createGraphics();
			g.drawImage(rawImage, 0,0,scaledWidth, rawImage.getHeight(), null);
			g.dispose();
		}
		return scaled;
	}
}
