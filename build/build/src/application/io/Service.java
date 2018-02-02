package application.io;

import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import application.converter.Converter;
import application.utility.Utility;
import javafx.scene.paint.Color;

public class Service {
	public void generateText(File i, File o, int blocks, int conversion, Color b, Color f) throws IOException{
		String outFileName;
		Converter.CONV_TYPE type = null;
		if(conversion >= 1){
			outFileName = Utility.omitExtension(i.getName())+"_"+i.hashCode()+"."+Utility.inferExtension(i.getName());
			if(Utility.inferExtension(i.getName()).equals("gif")){
				type = Converter.CONV_TYPE.IMG_GIF;
			}
			else{
				type = Converter.CONV_TYPE.IMG_OTHER;
			}
		}
		else{
			outFileName = Utility.omitExtension(i.getName())+"_"+i.hashCode()+".txt";
			type = Converter.CONV_TYPE.TXT;
		}
		Converter c = new Converter(i, 
				blocks, 
				type, 
				Utility.getAwtColorFromFXColor(b),
				Utility.getAwtColorFromFXColor(f));

		c.setOutFile(outFileName);
		c.convert(o);

	}

}
