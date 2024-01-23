package file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 * <h1>Download File From URL,</h1>
 * This Class Downloads File From URL
 * 
 * @author SauravPahuja
 * @version 1.0
 * @since 10-04-2020 
 */
public class FileDownloader {

	private static final int BUFFER_BYTES = 1024;
	private static boolean isDownlaoded = false;
	private static String fileNameWithExtension = "";
	private static String fileNameWithoutExtension = "";
	
	/**
	 * This is main method to start download along with progress bar
	 * @param args
	 * @return Nothing
	 */
	public static void main(String[] args) {
		isDownlaoded = downloadFile("http://enos.itcollege.ee/~jpoial/algorithms/GT/Data%20Structures%20and%20Algorithms%20in%20Java%20Fourth%20Edition.pdf");
		
		if(isDownlaoded == true && Objects.nonNull(fileNameWithExtension) && fileNameWithExtension.length() > 0) {
			System.out.println("Downloaded file -> "+ fileNameWithExtension);
		} else {
			System.out.println("Failed to download file, Please try again !");
		}
	}
	
	
	/**
	 * This method is used to get file name from URL
	 * to set default file name to file being downloaded if
	 * user doesn't provide any specified name
	 * 
	 * @param url This is URL of file to fetch name from
	 * @return String This return fileName from given URL
	 * @exception IndexOutOfBoundsException on wrong file name
	 * @see IndexOutOfBoundsException
	 */
	static String getFileNameFromURL(URL url) {
		String urlFilePath = Objects.nonNull(url) ? url.getPath() : "";
		
		try {
			if(urlFilePath.length() > 0) {
				fileNameWithExtension = urlFilePath.substring(urlFilePath.lastIndexOf('/')+1);
				fileNameWithoutExtension = fileNameWithExtension.substring(0,fileNameWithExtension.lastIndexOf('.'));
			}
		} catch(IndexOutOfBoundsException e) {
			System.out.println("Something is wrong with provided URL, Please verify it");
		}

		return fileNameWithExtension;
	}

	static boolean isWorking(String str) {
		return true;
	}
	
	
	/**
	 * This method is used to download file by providing correct URL
	 * otherwise it will raise a flag that download is failed
	 * 
	 * @param urlText
	 * @return boolean Whether file is successfully downloaded or not.
	 * @exception IOException On wrong URL
	 * @see IOException
	 */
	static boolean downloadFile(String urlText) {
		try {
			URL url = new URL(urlText);
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setRequestMethod("HEAD");
			
			long fileTotalSize = httpConnection.getContentLengthLong();
			
			fileNameWithExtension = getFileNameFromURL(url);
			File file = new File("E:\\"+fileNameWithExtension);
			
			//Checking file already exists on disk, If exists return false simply.
			if(file.exists()) {
				System.out.println("File with same name "+fileNameWithExtension+" already there on disk, "
						+ "\nCan't download duplicate file, "
						+"\nConsider changing file name and try again ! \n");
				return false;
			}
			
			try(BufferedInputStream in = new BufferedInputStream(url.openStream())) {
				FileOutputStream out = new FileOutputStream(file);
				byte[] dataBuffer = new byte[BUFFER_BYTES];
				int bytesRead;
				int downloaded = 0;
				while((bytesRead = in.read(dataBuffer, 0, BUFFER_BYTES)) != -1) {
					downloaded += bytesRead;
					
					System.out.print("Downloaded "+downloaded+"/"+fileTotalSize+"\r");
					
					out.write(dataBuffer, 0, bytesRead);
				}
				
				out.close();
				return true;
			} catch(Exception e) {
				return false;
			}
		} catch(IOException e) {
			System.out.println("Something is wrong with URL, Please Check !");
			return false;
		}
	}
}

