package file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

/**
 * <h1>Download File From URL</h1>
 * This Class Downloads File From URL Along With Progress Bar
 * 
 * @author SauravPahuja
 * @version 1.0
 * @since 10-04-2020 
 */
public class FileDownloaderWithProgressBar {

	private static final String DOWNLOAD_DIR_PATH = "E:\\";
	private static final int BUFFER_BYTES = 1024;
	private static final String URL = "https://download.macromedia.com/pub/elearning/objects/mx_creating_lo.pdf";
	private static final String ICON_LEFT = "[";
	private static final String ICON_RIGHT = "]";
	private static final String ICON_PROGRESS = "=";
	
	private static String fileNameWithExtension = "";
	
	/**
	 * This is main method to start download along with progress bar
	 * @param args
	 * @return Nothing
	 */
	public static void main(String[] args) {
		
		if(args.length !=0) {
			try {
				fileNameWithExtension = new String(args[0]);
			} catch(IndexOutOfBoundsException e) {
				System.out.println("Please provide valid argument to program");
			}
		}
		
		if(fileNameWithExtension == "") {
			System.out.println("You have not defined any name of file, Now file name will be considered from URL itself.");
			fileNameWithExtension = getFileNameFromURL(URL);
		}
		
		boolean isDownlaoded = fileNameWithExtension!="" ? downloadFile(URL) : false;
		
		if(isDownlaoded == true && Objects.nonNull(fileNameWithExtension) && fileNameWithExtension.length() > 0) {
			System.out.print("\nDownloaded file -> "+ fileNameWithExtension);
		} else {
			System.out.println("Failed to download file, Make sure you are connected to internet\nPlease try again !");
		}
	}
	
	
	/**
	 * This method is used to get file name from URL
	 * to set default file name to file being downloaded if
	 * user doesn't provide any specified name
	 * 
	 * @param urlText This is URL of file to fetch name from
	 * @return String This return fileName from given URL
	 * @exception IndexOutOfBoundsException on wrong file name
	 * @see IndexOutOfBoundsException
	 */
	static String getFileNameFromURL(String urlText) {
		
		URL url = null;
		
		try {
			url = new URL(urlText);
		} catch(MalformedURLException e) {
			System.out.println("Something wrong with this URL, Please check !");
		}
	
		String urlFilePath = Objects.nonNull(url) ? url.getPath() : "";

		try {
			if(urlFilePath.length() > 0) {
				fileNameWithExtension = urlFilePath.substring(urlFilePath.lastIndexOf('/')+1);
			}
		} catch(IndexOutOfBoundsException e) {
			System.out.println("Something is wrong with provided URL, Please verify it");
		}

		return fileNameWithExtension;
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
			
			File file = new File(DOWNLOAD_DIR_PATH+fileNameWithExtension);
			
			//Checking file already exists on disk, If exists return false simply.
			if(file.exists()) {
				System.out.println("File with same name "+fileNameWithExtension+" already there on disk, "
						+ "\nCan't download duplicate file, "
						+"\nConsider changing file name and try again ! \n");
				return false;
			}
			
			try(BufferedInputStream in = new BufferedInputStream(url.openStream())) {
				FileOutputStream out = new FileOutputStream(file);
				
				//We will download file byte by byte defined.
				byte[] dataBuffer = new byte[BUFFER_BYTES];
				int bytesRead;
				int downloaded = 0;
				int donePercent = 0;
				int currPercent = 0;

				StringBuilder sb = new StringBuilder();
				sb.append(ICON_LEFT);

				while((bytesRead = in.read(dataBuffer, 0, BUFFER_BYTES)) != -1) {
					out.write(dataBuffer, 0, bytesRead);
					downloaded += bytesRead;
					
					if(donePercent>currPercent) {
						sb.append(ICON_PROGRESS);
						currPercent++;
					}
					
					donePercent = (int) ((downloaded * 100) / fileTotalSize);
					System.out.print("\rDownloaded "+downloaded+"/"+fileTotalSize+" "+sb+ICON_RIGHT+" => "+donePercent+"%");
				}
				
				//To separate process with final message.
				System.out.println("");
				
				//Closing output stream
				out.close();
				
				return true;
			} catch(Exception e) {
				//Nothing to do, simply return false will lead to fail download.
				return false;
			}
		} catch(IOException e) {
			System.out.println("Something is wrong with URL, Please Check !");
			return false;
		}
	}
}