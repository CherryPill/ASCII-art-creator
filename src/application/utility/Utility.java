package application.utility;

public class Utility {
	public static String inferExtension(String fileName){
		return fileName.substring(fileName.lastIndexOf(".")+1);
	}
	public static String omitExtension(String fileName){
		String[] splitArr = fileName.split("\\.");
		System.out.println(splitArr[0]);
		return splitArr[0];
	}
}
