package application.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import application.converter.Converter;

public class Service {
	public void generateText(File i, File o, int blocks, int conversion) throws IOException{
		String outFileName;
		if(conversion == 1){
			outFileName = omitExtension(i.getName())+"_"+i.hashCode()+"."+inferExtension(i.getName());
		}
		else{
			outFileName = omitExtension(i.getName())+"_"+i.hashCode()+".txt";
		}
		BufferedImage inputImageData = ImageIO.read(i);
		Converter c = new Converter(inputImageData,blocks);
		char charMatrix[][] = c.convertToASCII();
		c.setOutFile(outFileName);
		c.writeImage(charMatrix, o);
	}
	private String inferExtension(String fileName){
		return fileName.substring(fileName.lastIndexOf("."));
	}
	private String omitExtension(String fileName){
		String[] splitArr = fileName.split("\\.");
		System.out.println(splitArr[0]);
		return splitArr[0];
	}
}
