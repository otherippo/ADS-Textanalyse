
import java.io.IOException;
import java.lang.String;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;

public class TextAnalyse {
	private static String path = "C:/Users/Oliver/Mega/Eclipse Oxygen/Mein Workspace/ADS-Textanalyse";
	//private static String teststring = " This book really changed the way I see different things in the world.  It also instilled a greater love in me, and questioned a lot of my thoughts and actions.  A must read for those who want to grow in intimacy with  Christ!     ";
	private static String testresult;
	private static int docCount;
	private static double docSize;
	private static int docLength;
	private static int docChar;
	private static String[] wordsArray;
	private static int docUniqueWords;
	
	public static void main(String[] args) throws IOException {
		//String mystring = "dies ist ein string, oh yeah.";
		//String content = readFile("Life Changing.txt");
		//String[] array = content.split("[\\s,\\.]+");
		//System.out.println(array[1]);
		//System.out.println(Arrays.toString(teststring.split("[\\s,\\.]+"))); //.split("[\\s,\\.]+"));
		System.out.println(Arrays.toString(cleanArray(true)));
		getBasicInfo();

	}
	
	private static String[] cleanArray(Boolean cleaned) throws IOException{
		String[] result;
		if (cleaned) {
			result = removeNull(readAllFiles().split("[\\s,\\.]+"));
		}
		else {
			result = removeNull(readAllFiles().split("[\\s,\\.]+"));
		}
		return result;
	}
	
	//method to parse text file
	private static String readFile(String path) throws IOException{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, StandardCharsets.UTF_8);
	}
	
	//read all files in path and parse them calling readFile for every file in directory
	//returns content of all files as string
	public static String readAllFiles() throws IOException{
		Files.walk(Paths.get(path)).filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".txt")).forEach(
				filePath ->{
				try {
					//System.out.println(readFile(filePath.getFileName().toString()));
					testresult += readFile(filePath.getFileName().toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		return testresult;
	}
	
	private static String[] removeNull(String[] array){
		return Arrays.stream(array)
        .filter(s -> (s != null && s.length() > 0))
        .toArray(String[]::new);
	}
	
	private static int uniqueWords(String[] array){
		HashSet<String> uniqueWords = new HashSet<>(Arrays.asList(array));
		return uniqueWords.size();
	}
	
	public static void getBasicInfo() throws IOException{
		
		Files.walk(Paths.get(path)).filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".txt")).forEach(
				filePath ->{
				try {
					wordsArray = removeNull(readFile(filePath.getFileName().toString()).split("[\\s,\\.]+"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				docCount++;
				try {
					docSize += Files.size(filePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				docLength += wordsArray.length;
				try {
					docChar += readFile(filePath.getFileName().toString()).length();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				docUniqueWords = uniqueWords(wordsArray);
				
			});
		//return docCount;
		System.out.println("********** STATISTIK : **********");
		System.out.println("Anzahl Dokumente: "+docCount);
		System.out.println("Dokumentengrösse total: "+docSize/1000+" KB");
		System.out.println("Anzahl Wörter: "+docLength);
		System.out.println("Anzahl unterschiedliche Wörter: "+docUniqueWords);
		System.out.println("Durchschnittliche Anzahl Zeichen (inkl. Leerzeichen): "+docChar/docCount); //inkl. Leerzeichen!
	}
	
}
