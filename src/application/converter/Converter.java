package application.converter;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Converter {
	private static final String asciiChars = "-+^=>?$&@#";
	private static final int blockSize = 16;
	private static void printImage(char arr[][], int newDimWidth, int newDimHeight) throws IOException{
		File textImage = new File("Z:\\ascii_test\\out.txt");
		FileWriter fw = new FileWriter(textImage);
		BufferedWriter bw = new BufferedWriter(fw); 
		System.out.println(newDimWidth+" "+newDimHeight);
		for(int y = 0;y<newDimHeight;y++){
			for(int x = 0;x<newDimWidth;x++){
				bw.write(arr[y][x]+" ");
			}
			bw.write("\r\n");
		}
		bw.close();
	}
	public static void convertToASCII(BufferedImage br) throws IOException{
		
		int blockCountForWidth = (br.getWidth()/blockSize);
		int blockCountForHeight = (br.getHeight()/blockSize);
		
		char charMatrix[][] = new char[blockCountForHeight][blockCountForWidth];
		
		System.out.println(blockCountForWidth+" "+blockCountForHeight);
		
		for(int x = 1;x<=blockCountForWidth;x++){
			for(int y = 1;y<=blockCountForHeight;y++){
				//get pixels of this block
				//get greyscale
				int endPointX = x*blockSize;
				int endPointY = y*blockSize;
				int startingPointX = endPointX - blockSize;
				int startingPointY = endPointY - blockSize;
				
			
				int avgGreyScaleForBlock = getAvgGS(startingPointX, startingPointY, endPointX, endPointY, br);
				
				charMatrix[y-1][x-1] = getCorrespondingChar(avgGreyScaleForBlock);
			}
		}
		printImage(charMatrix, blockCountForWidth, blockCountForHeight);
		//processing each block and computing greyscale
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
				//obtain gray pixel
				int greyPixel = (r+g+b)/3;
				scaleAccumulator+=greyPixel;
			}
		}
		avg = scaleAccumulator / pixelsCount;
		return avg;
	}
}
