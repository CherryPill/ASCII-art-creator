package application.converter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import application.encoder.GifEncoder;
import application.utility.GifUtility;
import application.utility.Utility;

public class Converter {
	
	public enum CONV_TYPE {TXT, IMG_OTHER, IMG_GIF};
	private final float scaleFactor = 1.5F;
	private static final String asciiChars = "-+^=>?$&@#";
	
	private final List<BufferedImage> inputImages;
	
	//private final BufferedImage inputImage;
	
	private static int gifDelayTime;
	
	private final int blockSize;
	
	private int blockCountForWidth;
	
	private int blockCountForHeight;
	
	private String outFile;
	
	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}
	
	
	public void writeImage(List<FrameText> frames, File out, Converter.CONV_TYPE cType) throws FileNotFoundException, IOException{
		File modifiedOutPath = new File(out.getAbsolutePath()+"\\"+outFile);
		List<BufferedImage> imageFrames = new ArrayList<>();
		int charSize = 20;
		switch(cType){
			case IMG_GIF:{
				
				for(FrameText ft:frames){
	
					int yOffset = charSize;
					
					int newImageWidth = blockCountForWidth*((charSize));
					int newImageHeight = blockCountForHeight*(charSize);
					BufferedImage outImage = new BufferedImage(newImageWidth, 
							newImageHeight, 
							BufferedImage.TYPE_INT_RGB);
					Graphics g = outImage.getGraphics();
					g.setColor(Color.WHITE);
					g.fillRect(0, 0, newImageWidth, newImageHeight);
					Font f = new Font("Monospaced",Font.PLAIN, charSize);
					g.setFont(f);
					int currYOffset = 0;
					g.setColor(Color.BLACK);
					String lastLine = null;
					for(int y = 0;y<blockCountForHeight;y++){
						StringBuilder line = new StringBuilder();
						for(int x = 0;x<blockCountForWidth;x++){
							 line.append(ft.getCharMatrix()[y][x]);
						}
						g.drawString(line.toString(), 0, currYOffset);
						currYOffset+=yOffset;
						lastLine = line.toString();
					}
					
					outImage = outImage.getSubimage(0, 0, g.getFontMetrics().stringWidth(lastLine), newImageHeight);
					
					imageFrames.add(outImage);
					
					//ImageIO.write(outImage,Utility.inferExtension(modifiedOutPath.getAbsolutePath()),modifiedOutPath);
					
				}
				ImageOutputStream outStream = new FileImageOutputStream(modifiedOutPath);
				GifEncoder ge = new GifEncoder(outStream,imageFrames.get(0).getType(), this.gifDelayTime);
				ge.encode();
				
				for(BufferedImage frame:imageFrames){
					ge.write(frame);
				}
				ge.dispose();
				break;
			}
			case IMG_OTHER:{
				int yOffset = charSize;
				
				int newImageWidth = blockCountForWidth*((charSize));
				int newImageHeight = blockCountForHeight*(charSize);
				BufferedImage outImage = new BufferedImage(newImageWidth, 
						newImageHeight, 
						BufferedImage.TYPE_INT_RGB);
				Graphics g = outImage.getGraphics();
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, newImageWidth, newImageHeight);
				Font f = new Font("Monospaced",Font.PLAIN, charSize);
				g.setFont(f);
				int currYOffset = 0;
				g.setColor(Color.BLACK);
				String lastLine = null;
				for(int y = 0;y<blockCountForHeight;y++){
					StringBuilder line = new StringBuilder();
					for(int x = 0;x<blockCountForWidth;x++){
						 line.append(frames.get(0).getCharMatrix()[y][x]);
					}
					g.drawString(line.toString(), 0, currYOffset);
					currYOffset+=yOffset;
					lastLine = line.toString();
				}
				
				outImage = outImage.getSubimage(0, 0, g.getFontMetrics().stringWidth(lastLine), newImageHeight);
				
				ImageIO.write(outImage,Utility.inferExtension(modifiedOutPath.getAbsolutePath()),modifiedOutPath);
				break;
			}
			case TXT:{
				FileWriter fw = new FileWriter(modifiedOutPath);
				BufferedWriter bw = new BufferedWriter(fw); 
				for(int y = 0;y<blockCountForHeight;y++){
					for(int x = 0;x<blockCountForWidth;x++){
						bw.write(frames.get(0).getCharMatrix()[y][x]);
					}
					bw.write("\r\n");
				}
				bw.close();
				break;
			}
		}
	}
	
	//handle txt too lol
	//TODO write image for list
	/*
	public void writeImage(char arr[][], File out, Converter.CONV_TYPE cType) throws IOException{
		File modifiedOutPath = new File(out.getAbsolutePath()+"\\"+outFile);
		switch(cType){
		case IMG:{
			int charSize = 20;
			int yOffset = charSize;
			
			int newImageWidth = blockCountForWidth*((charSize));
			int newImageHeight = blockCountForHeight*(charSize);
			BufferedImage outImage = new BufferedImage(newImageWidth, 
					newImageHeight, 
					BufferedImage.TYPE_INT_RGB);
			Graphics g = outImage.getGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, newImageWidth, newImageHeight);
			Font f = new Font("Monospaced",Font.PLAIN, charSize);
			g.setFont(f);
			int currYOffset = 0;
			g.setColor(Color.BLACK);
			String lastLine = null;
			for(int y = 0;y<blockCountForHeight;y++){
				StringBuilder line = new StringBuilder();
				for(int x = 0;x<blockCountForWidth;x++){
					 line.append(arr[y][x]);
				}
				g.drawString(line.toString(), 0, currYOffset);
				currYOffset+=yOffset;
				lastLine = line.toString();
			}
			outImage = outImage.getSubimage(0, 0, g.getFontMetrics().stringWidth(lastLine), newImageHeight);
			ImageIO.write(outImage,Utility.inferExtension(modifiedOutPath.getAbsolutePath()),modifiedOutPath);
			break;
		}
		case IMG_GIF:{
			List<BufferedImage> frames = new ArrayList<BufferedImage>();
			//ImageInputStream ims = ImageIO.createImageInputStream(arg0)//move to constructor
			break;
		}
		case TXT:{
			FileWriter fw = new FileWriter(modifiedOutPath);
			BufferedWriter bw = new BufferedWriter(fw); 
			for(int y = 0;y<blockCountForHeight;y++){
				for(int x = 0;x<blockCountForWidth;x++){
					bw.write(arr[y][x]);
				}
				bw.write("\r\n");
			}
			bw.close();
			break;
		}
		}
		
	}
*/
	//reads image and puts its frames into a list, if gif then it has multiple frames, if not it has a single frame
	public static List<BufferedImage> readImage(File imgSource){
		List<BufferedImage> frames = new ArrayList<>();
		String format = Utility.inferExtension(imgSource.getName());
		if(format.equals("gif")){
			try{
				ImageReader reader = ImageIO.getImageReadersByFormatName(format).next();
				ImageInputStream stream = ImageIO.createImageInputStream(imgSource);
				reader.setInput(stream);
				gifDelayTime = GifUtility.getGifDelay(reader);
				int frameCount = reader.getNumImages(true);
				for(int i = 0;i<frameCount;i++){
					BufferedImage frame = reader.read(i);
					if(frame != null){
						frames.add(frame);
					}
				}	
			}
			catch(IOException e){
				
			}
			
		}
		else{
			try {
				BufferedImage inputImageData = ImageIO.read(imgSource);
				frames.add(inputImageData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return frames;
	}
	
	public Converter(List<BufferedImage> imageData, int blocks){
		
		inputImages = scaleImages(imageData);

		System.out.println("NEW:"+inputImages.get(0).getWidth());
		blockSize = blocks;
	}
	
	public List<FrameText> convertToASCII() throws IOException{
		List<FrameText> charFrames = new ArrayList<>();
		for(BufferedImage frame:inputImages){

			blockCountForWidth = (frame.getWidth()/blockSize);
			blockCountForHeight = (frame.getHeight()/blockSize);
			
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
					
					int avgGreyScaleForBlock = getAvgGS(startingPointX, startingPointY, endPointX, endPointY, frame);
					
					charMatrix[y-1][x-1] = getCorrespondingChar(avgGreyScaleForBlock);
				}
			}
			charFrames.add(new FrameText(charMatrix));
		}
		return charFrames;
	}

	/*
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
*/
	private static char getCorrespondingChar(int i){
		int reqIndex = i % asciiChars.length();
		return asciiChars.charAt(reqIndex);
	}
	
	private int getAvgColor(int x, int y, int w, int h, BufferedImage img){
		return 1;
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
	private List<BufferedImage> scaleImages(List<BufferedImage> frames){
		ListIterator<BufferedImage> imageIterator = frames.listIterator();
		while(imageIterator.hasNext()){
			imageIterator.set(scaleImage(imageIterator.next()));
		}
		return frames;
	}
	
	private BufferedImage scaleImage(BufferedImage rawImage){
		BufferedImage scaled = null;
		if(rawImage != null){
			int scaledWidth = (int) (rawImage.getWidth()*scaleFactor);
			scaled = new BufferedImage(scaledWidth, rawImage.getHeight(), rawImage.getType());
			Graphics2D g = scaled.createGraphics();
			g.drawImage(rawImage, 0, 0, scaledWidth, rawImage.getHeight(), null);
			g.dispose();
		}
		return scaled;
	}
}
