import java.io.IOException;
import java.lang.String;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TextAnalyse {
	private static String path = "C:/Users/Oliver/Mega/Eclipse Oxygen/Mein Workspace/ADS-Textanalyse";
	//private static String teststring = " This book really changed the way I see different things in the world.  It also instilled a greater love in me, and questioned a lot of my thoughts and actions.  A must read for those who want to grow in intimacy with  Christ!     ";
	private static String testresult;
	private static int docCount;
	private static double docSize;
	private static int docLength;
	private static int docSingleLength;
	private static int docChar;
	private static String[] wordsArray;
	private static int docUniqueWords;
	private static HashSet<String> uniqueWords = new HashSet<>();
	private static Map<String, Integer> wordWasFoundIn = new HashMap<String, Integer>();
	private static Map<String, Integer> uniqueWordsOcc = new HashMap<String, Integer>();
	private static Map<String, Integer> allDocLength = new HashMap<String, Integer>();
	private static String fileName;
	private static String wantedWord = "book"; // <-- define word to search for here
	private static String findWordInFileNot = "Keine Treffer für \""+wantedWord+"\"";
	//private static ArrayList<String> wordWasFoundInFilename = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException {
		//String mystring = "dies ist ein string, oh yeah.";
		//String content = readFile("Life Changing.txt");
		//String[] array = content.split("[\\s,\\.]+");
		//System.out.println(array[1]);
		//System.out.println(Arrays.toString(teststring.split("[\\s,\\.]+"))); //.split("[\\s,\\.]+"));
		//System.out.println(Arrays.toString(cleanArray(true)));
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
		uniqueWords = new HashSet<>(Arrays.asList(array));
		return uniqueWords.size();
	}
	
	private static void getAllDocLength(String nameoffile, int length) {
		allDocLength.put(path+"/"+nameoffile, length);
	}
	
	//lese array und erstelle hashmap; array-element z.b. wort ist key, value wird dann zur häufigkeit (occurrence)
	//falls wort (key) schon in hashmap, erhöhe value (häufigkeit) um 1
	//anderenfalls nimm wort auf (wort ist key) und setze value auf 1 (1x vorgekommen bis jetzt)
	private static void getContentDetails(String[] array){
		for (int i=0; i<array.length; i++) {
			getWordOccurrence(array[i]);
			findWordInFile(array[i]);
		}
	}
	
	private static void findWordInFile(String arrayElement) {
		if (arrayElement.equals(wantedWord)) {
			hashmContainKey(wordWasFoundIn, path+"/"+fileName+":"+arrayElement);
			findWordInFileNot = ""; //if word was found in file there is no need to print out "no matches"
		}
	}
	
	private static void getWordOccurrence(String arrayElement) {
		hashmContainKey(uniqueWordsOcc, arrayElement);		
	}
	
	private static void hashmContainKey(Map<String, Integer> hashmap, String arrayElement) {
		if (hashmap.containsKey(arrayElement)) {
			int value = hashmap.get(arrayElement);
			hashmap.put(arrayElement, value+1);
		}
		else {
			hashmap.put(arrayElement, 1);
		}
	}
	
	private static String printHashMap(Map<String, Integer> hashmap, String before, String after) {
		String result = "";
		Set<Entry<String, Integer>> hashSet=hashmap.entrySet();
        for(Entry<String, Integer> entry:hashSet ) {
            result += before+": "+entry.getKey()+"   |   "+after+": "+entry.getValue()+"\n";
            docUniqueWords++; //for every entry in hashmap increase by 1 to get the mnumber off unique words
        }
        return result;
	}
	
	public static void getBasicInfo() throws IOException{
		
		Files.walk(Paths.get(path)).filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".txt")).forEach(
				filePath ->{
				fileName = filePath.getFileName().toString();
				try {
					wordsArray = removeNull(readFile(fileName).split("[\\s,\\.]+"));
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
				docSingleLength = wordsArray.length;
				docLength += docSingleLength;
				try {
					docChar += readFile(fileName).length();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//wordsArrayList.addAll(Arrays.asList(wordsArray));
				//docUniqueWords += uniqueWords(wordsArray); returns unique words from file 1 + unique words from file 2 etc -> wrong!
				getContentDetails(wordsArray);
				getAllDocLength(fileName, docSingleLength);
				
			});
		
		//return docCount;
		String wordOcc = printHashMap(uniqueWordsOcc,"Wort","Häufigkeit");
		System.out.println("********** STATISTIK : **********");
		System.out.println("Anzahl Dokumente: "+docCount);
		System.out.println("Dokumentengrösse total: "+docSize/1000+" KB");
		System.out.println("Anzahl Wörter: "+docLength);
		System.out.println("Anzahl unterschiedliche Wörter: "+docUniqueWords);
		System.out.println("Durchschnittliche Anzahl Zeichen (inkl. Leerzeichen): "+docChar/docCount); //inkl. Leerzeichen!
		System.out.println("Häufigkeit aller Wörter: \n"+wordOcc);
		System.out.println("Anzahl Wörter jedes Dokuments: \n"+printHashMap(allDocLength,"Datei","Anzahl Wörter"));
		
		//word to search for needs to be defined at line 32
		System.out.println("Die Wortsuche ergab folgende Treffer: \n"+findWordInFileNot+printHashMap(wordWasFoundIn,"Datei:Keyword","Gefunden"));
		
		//System.out.println("values von uniqueWordsOcc: "+uniqueWordsOcc.values());
	}
	
}
