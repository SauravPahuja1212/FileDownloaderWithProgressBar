package file;

/**
 *  This file can be used with a file
 *  to show the progress bar (Uploading or Downloading)
 *  
 * @author SauravPahuja
 * @version 1.0
 * @since 10-04-2020
 */
public class ProgressBarIndicator {
	
	/**
	 * This method is used to indicate progress as
	 * calculated by doneSize and totalSize parameters
	 * 
	 * @param doneSize
	 * @param totalSize
	 * @return Nothing
	 */
	static void indicateProgress(int doneSize, long totalSize) {
		//char[] animatedChars = new char[]{'|','/','-','\\'};
		
		String iconLeft = "[";
		String iconRight = "]";
		String iconDone = "=";
		
		int donePercent = (int) ((doneSize * 100) / totalSize);
		
		System.out.println(donePercent);
		
		StringBuilder sb = new StringBuilder();
		sb.append(iconLeft);

		for(int i=0; i<=donePercent; i++) {
			sb.append(iconDone);
			System.out.print(sb+iconRight+" => "+i+"%"+"\r");
		}
		System.out.println(sb.toString());
	}
}
