package Java.Zone.Utils;

import java.io.IOException;

public class FileUtils {
	//--------------------���ļ���----------------------
	public  static void openFolder(String folderPath){
		try {
			Runtime.getRuntime().exec("cmd /c start "+folderPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
