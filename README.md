# JavaProjects  

Info on Projects:
## ImageChecker
Moves an image from any windows directory into a locally stored "image folder", if the image(in name) already exists in the folder it will move the existing image into an archive folder and the selected image into the archive folder

			Is built purely using Java  
			Input(Takes in an image using a directory through command line)  
					Examples: 
						C:\Users\home\Desktop\JavaProject\ImageChecker\image\dog.png
						C:\Users\home\Desktop\index.png
			Assumptions:
				The ImageChecker generally works with any file but I limited it to the most popular know image types  
				jpeg(jpg), png, gif, tiff, psd, pdf, eps, ai, svg, indd, raw, svg  
				The "Archive Folder" and "Image Folder" are located within the ImageChecker folder  
				If the image file exists in archive's folder it is overwritten  

								
## JsonToExcel  
Takes a json file as input where java parses the data and creates an xls file and txt file containing the content of the json file  
Uses the [json.jar](https://repo1.maven.org/maven2/org/json/json/20200518/json-20200518.jar/) file and Maven Dependencies of **org.apache.poi poi** and **org.apache.poi ooxml**


			Input(Takes in a json file using a directory through command line)
				Example: 
				C:\Users\Soul\Desktop\people.json

			Ouput:
				Example if we take in C:\Users\Soul\Desktop\people.json
				The output file will be "people.xls" => name_of_file.xls
				With the contents of the json file
				It will also print out a file pipe delimited as "people_output".txt => fileName_output.txt


			
			Assumptions:
				Json File is formatted to the following Structure => JSONObj to JsonArray of JSONObjs
				{
					"something": [	
							{ "Test": "Yes",
							  "Best": "Fast"
							}
					]
				}
			

				
