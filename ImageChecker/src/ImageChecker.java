import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ImageChecker{
	public static void main(String args[]){
		System.out.println("========================================");
		commands();
		System.out.println("\nType in the image's file directory \n" + 
				"Example: C:\\Users\\home\\Desktop\\JavaProject\\ImageChecker\\image\\dog.png" + "\n");
		while(true){

		Scanner scanner = new Scanner(System.in);
		String imageDirectory = scanner.nextLine();
		imageDirectory = imageDirectory.trim();
		//System.out.println(imageDirectory);
		
		if(imageDirectory.equals("exit")){
			System.out.println("Program is being terminated ....");
			break;
			}
		else if(imageDirectory.equals("printImageFolder")){
			printImageFolderFiles();
			continue;
			}
		else if(imageDirectory.equals("printArchiveFolder")){
			printArchiveFolderFiles();
			continue;
		}
		
		
		File imageFile = new File(imageDirectory);
		if(imageFile.exists()){
			
			if(isImage(imageDirectory)){
			//System.out.println("The File exists");
				//File imgPath = new File(".\\image");
				if(imageFinder(imageFile)){
					System.out.println("File already exists in the image folder");
					System.out.println("Attempting to move file in archive folder");
					moveToArchiveFolder(imageFile);
				}else{
					System.out.println("The image doesn't exist in the image folder.\nAdding the file now");
					moveToImageFolder(imageFile);
				}
			}
			else{
				System.out.println("Not an image file\n" + "Please insert an image file with the following extensions: "
						+ "\n\tjpeg(jpg), png, gif, tiff, psd, pdf, eps, ai, svg, indd, raw, svg");
			}
			
		} else{
			System.out.println("The given directory/file does not exist or the file's existence cannot be verified.");
		}
		
		

		}
		
		System.out.println("========================================");
		System.out.println("Program has been terminated.");
	}
	
	
	
	private static void commands() {
		System.out.println("Commands: ");
		System.out.println("\t Type 'printImageFolder' => print all files in the image folder");
		System.out.println("\t Type 'printArchiveFolder' => print all files in the archive folder");
		System.out.println("\t Type 'exit' to terminate the program");

		
	}



	//Prints out all files in the image Directory
	public static void printImageFolderFiles(){
		System.out.println("Printing out all files in the image folder");
		Path path = Paths.get(".\\image");
		try(Stream<Path> subPaths = Files.walk(path, 1)){
			subPaths.forEach(System.out::println);
		} catch (IOException e){
			e.printStackTrace();
		}
		
		return;
	}
	
	
	public static boolean isImage(String imgName){
		
		if(imgName.endsWith(".jpeg") || imgName.endsWith(".jpg") || imgName.endsWith(".png") || imgName.endsWith(".tiff") ||
				imgName.endsWith(".gif") || imgName.endsWith(".psd") || imgName.endsWith(".pdf") || imgName.endsWith(".eps") ||
				imgName.endsWith(".ai") || imgName.endsWith(".indd") || imgName.endsWith(".raw") || imgName.endsWith(".svg")){
			return true;
		}
		
		return false;
	}
	//Prints out all files in the archive Directory
	public static void printArchiveFolderFiles(){
		System.out.println("Printing out all files in the archive folder");
		Path path = Paths.get(".\\archive");
		
		try(Stream<Path> subPaths = Files.walk(path, 1)){
			subPaths.forEach(System.out::println);
		} catch (IOException e){
			e.printStackTrace();
		}
		System.out.println();
		return;
	}
	
	//Instead of adding the file to the image folder it will move the file to the archive folder
	public static void moveToArchiveFolder(File imageFile){
		
		//System.out.println("imgFile" + imageFile );
		
		File imgPath = new File(".\\image");
		File originalImage = null;
		
		for(File file: imgPath.listFiles()){
			//System.out.println("File Name: " + file.getName());
			//If the 'new' image is found in the folder
			if(file.getName().equals(imageFile.getName())){
				//System.out.println("Image Found");
				originalImage = file;
				break;
			}
		}
		
		Path archiveFolder = Paths.get(".\\archive"+ "\\"+originalImage.getName());
		Path imageFolder = Paths.get(".\\image"+ "\\"+ originalImage.getName());

		Path originalImagePath = Paths.get(originalImage.toString());
		Path ImagePath = Paths.get(imageFile.toString());
		System.out.println("FileName " + originalImage);
		try{
			Files.move(originalImagePath, archiveFolder, StandardCopyOption.REPLACE_EXISTING );
			System.out.println("Original image in image folder moved to archive folder");
			Files.move(ImagePath, imageFolder, StandardCopyOption.REPLACE_EXISTING );
			System.out.println("New image moved into image folder");

		} catch (IOException e){
			e.printStackTrace();
		}
		
		return;
	}
	
	
	//Moves the image to the Image folder if not found
	public static void moveToImageFolder(File imageFile){
		Path imageFolder = Paths.get(".\\image"+ "\\"+ imageFile.getName());
		Path ImagePath = Paths.get(imageFile.toString());

		try{
			Files.move(ImagePath, imageFolder, StandardCopyOption.REPLACE_EXISTING );
			System.out.println("New image moved into image folder");

		} catch (IOException e){
			e.printStackTrace();
		}
		
		return;
	}
	
	//Traverses the image folder, if the given file is found return True
	public static boolean imageFinder(File imageFile){
		//System.out.println("Image Name: " + imageFile.getName());
		File imgPath = new File(".\\image");
		
		for(File file: imgPath.listFiles()){
			//System.out.println("File Name: " + file.getName());
			//If the 'new' image is found in the folder
			if(file.getName().equals(imageFile.getName())){
				//System.out.println("Image Found");
				return true;
			}
		}
		//File doesn't exist in image folder
		return false;
	}
}