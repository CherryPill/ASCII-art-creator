package application.io;

import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import application.converter.Converter;
import application.converter.FrameText;
import application.utility.Utility;

public class Service {
	public void generateText(File i, File o, int blocks, int conversion) throws IOException{
		String outFileName;
		
		if(conversion >= 1){
			outFileName = Utility.omitExtension(i.getName())+"_"+i.hashCode()+"."+Utility.inferExtension(i.getName());
		}
		else{
			outFileName = Utility.omitExtension(i.getName())+"_"+i.hashCode()+".txt";
		}
		List<BufferedImage> imageData = Converter.readImage(i);
		Converter c = new Converter(imageData,blocks);
		
		List<FrameText> charMatrices = c.convertToASCII();
		
		//char charMatrix[][] = c.convertToASCII();
		c.setOutFile(outFileName);
		
		String targetFileType = Utility.inferExtension(outFileName);
		if(targetFileType.equals("gif")){
			c.writeImage(charMatrices, o, Converter.CONV_TYPE.IMG_GIF);
		}
		else if(targetFileType.equals("jpg")){
			c.writeImage(charMatrices, o, Converter.CONV_TYPE.IMG_OTHER);
		}
		else{
			c.writeImage(charMatrices, o, Converter.CONV_TYPE.TXT);
		}
		
	}

}
