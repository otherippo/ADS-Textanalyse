import java.io.IOException;
import java.lang.String;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import me.xdrop.fuzzywuzzy.Applicable;
import me.xdrop.fuzzywuzzy.FuzzySearch;

public class TextAnalyse {
	private static String path = "C:/Users/Oliver/Mega/Eclipse Oxygen/Mein Workspace/ADS-Textanalyse";
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
	private static Map<String, Double> allDocSizes = new HashMap<String, Double>();
	private static Map<String, String[]> allDocs = new HashMap<String, String[]>();
	private static Map<String, Integer> allDocsResult = new HashMap<String, Integer>();
	private static String[] stopWords = {"a","a's","able","about","above","according","accordingly","across","actually","after","afterwards","again","against","ain't","all","allow","allows","almost","alone","along","already","also","although","always","am","among","amongst","an","and","another","any","anybody","anyhow","anyone","anything","anyway","anyways","anywhere","apart","appear","appreciate","appropriate","are","aren't","around","as","aside","ask","asking","associated","at","available","away","awfully","b","be","became","because","become","becomes","becoming","been","before","beforehand","behind","being","believe","below","beside","besides","best","better","between","beyond","both","brief","but","by","c","c'mon","c's","came","can","can't","cannot","cant","cause","causes","certain","certainly","changes","clearly","co","com","come","comes","concerning","consequently","consider","considering","contain","containing","contains","corresponding","could","couldn't","course","currently","d","definitely","described","despite","did","didn't","different","do","does","doesn't","doing","don't","done","down","downwards","during","e","each","edu","eg","eight","either","else","elsewhere","enough","entirely","especially","et","etc","even","ever","every","everybody","everyone","everything","everywhere","ex","exactly","example","except","f","far","few","fifth","first","five","followed","following","follows","for","former","formerly","forth","four","from","further","furthermore","g","get","gets","getting","given","gives","go","goes","going","gone","got","gotten","greetings","h","had","hadn't","happens","hardly","has","hasn't","have","haven't","having","he","he's","hello","help","hence","her","here","here's","hereafter","hereby","herein","hereupon","hers","herself","hi","him","himself","his","hither","hopefully","how","howbeit","however","i","i'd","i'll","i'm","i've","ie","if","ignored","immediate","in","inasmuch","inc","indeed","indicate","indicated","indicates","inner","insofar","instead","into","inward","is","isn't","it","it'd","it'll","it's","its","itself","j","ju","","k","keep","keeps","kept","know","knows","known","l","last","lately","later","latter","latterly","least","less","lest","let","let's","like","liked","likely","little","look","looking","looks","ltd","m","mainly","many","may","maybe","me","mean","meanwhile","merely","might","more","moreover","most","mostly","much","must","my","myself","n","name","namely","nd","near","nearly","necessary","need","needs","neither","never","nevertheless","new","next","nine","no","nobody","non","none","noone","nor","normally","not","nothing","novel","now","nowhere","o","obviously","of","off","often","oh","ok","okay","old","on","once","one","ones","only","onto","or","other","others","otherwise","ought","our","ours","ourselves","out","outside","over","overall","own","p","particular","particularly","per","perhaps","placed","please","plus","possible","presumably","probably","provides","q","que","quite","qv","r","rather","rd","re","really","reasonably","regarding","regardless","regards","relatively","respectively","right","s","said","same","saw","say","saying","says","second","secondly","see","seeing","seem","seemed","seeming","seems","seen","self","selves","sensible","sent","serious","seriously","seven","several","shall","she","should","shouldn't","since","six","so","some","somebody","somehow","someone","something","sometime","sometimes","somewhat","somewhere","soon","sorry","specified","specify","specifying","still","sub","such","sup","sure","t","t's","take","taken","tell","tends","th","than","thank","thanks","thanx","that","that's","thats","the","their","theirs","them","themselves","then","thence","there","there's","thereafter","thereby","therefore","therein","theres","thereupon","these","they","they'd","they'll","they're","they've","think","third","this","thorough","thoroughly","those","though","three","through","throughout","thru","thus","to","together","too","took","toward","towards","tried","tries","truly","try","trying","twice","two","u","un","under","unfortunately","unless","unlikely","until","unto","up","upon","us","use","used","useful","uses","using","usually","uucp","v","value","various","very","via","viz","vs","w","want","wants","was","wasn't","way","we","we'd","we'll","we're","we've","welcome","well","went","were","weren't","what","what's","whatever","when","whence","whenever","where","where's","whereafter","whereas","whereby","wherein","whereupon","wherever","whether","which","while","whither","who","who's","whoever","whole","whom","whose","why","will","willing","wish","with","within","without","won't","wonder","would","would","wouldn't","x","y","yes","yet","you","you'd","you'll","you're","you've","your","yours","yourself","yourselves","z","zero"};
	private static List<String> stopWordsList = Arrays.asList(stopWords);
	private static Map<String, Integer> importantWords = new HashMap<String, Integer>();
	
	public static void main(String[] args) throws IOException {
		getBasicInfo();
		commandoCenter();
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
		uniqueWordsOcc = sortByKey(uniqueWordsOcc);
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
            docUniqueWords++; //for every entry in hashmap increase by 1 to get the number off unique words
        }
        return result;
	}
	
	private static String allDocSizesCompare(String compare) {
		String[] array = removeNull(compare.split("[\\s]+"));
		String result = "";
 		Set<Entry<String, Double>> hashSet=allDocSizes.entrySet();
         for(Entry<String, Double> entry:hashSet ) {
        	 switch(array[0]) {
        	 case "all" :
        		 result += entry.getKey()+", "+entry.getValue()+" KB \n";
        		 break;
        	 case "<" :
        		 if (array.length == 2) {
        			 if (entry.getValue() < Double.parseDouble(array[1])) {
            			 result += entry.getKey()+", "+entry.getValue()+" KB \n";
                	 }
        		 }
        		 else if (array[2].equals("<")) {
        			 if (entry.getValue() < Double.parseDouble(array[1]) && entry.getValue() < Double.parseDouble(array[3])) {
            			 result += entry.getKey()+", "+entry.getValue()+" KB \n";
                	 }
        		 }
        		 else if (array[2].equals(">")) {
        			 if (entry.getValue() < Double.parseDouble(array[1]) && entry.getValue() > Double.parseDouble(array[3])) {
            			 result += entry.getKey()+", "+entry.getValue()+" KB \n";
                	 }
        		 }
        		 else if (array[2].equals("=")) {
        			 if (entry.getValue() < Double.parseDouble(array[1]) && entry.getValue() == Double.parseDouble(array[1])) {
            			 result += entry.getKey()+", "+entry.getValue()+" KB \n";
                	 }
        		 }
        		 break;
        	 case ">" :
        		 if (array.length == 2) {
        			 if (entry.getValue() > Double.parseDouble(array[1])) {
            			 result += entry.getKey()+", "+entry.getValue()+" KB \n";
                	 }
        		 }
        		 else if (array[2].equals(">")) {
        			 if (entry.getValue() > Double.parseDouble(array[1]) && entry.getValue() > Double.parseDouble(array[3])) {
            			 result += entry.getKey()+", "+entry.getValue()+" KB \n";
                	 }
        		 }
        		 else if (array[2].equals("<")) {
        			 if (entry.getValue() > Double.parseDouble(array[1]) && entry.getValue() < Double.parseDouble(array[3])) {
            			 result += entry.getKey()+", "+entry.getValue()+" KB \n";
                	 }
        		 }
        		 else if (array[2].equals("=")) {
        			 if (entry.getValue() > Double.parseDouble(array[1]) && entry.getValue() == Double.parseDouble(array[1])) {
            			 result += entry.getKey()+", "+entry.getValue()+" KB \n";
                	 }
        		 }
        		 break;
        	 case "=" :
        		 if (array.length == 2) {
        			 if (entry.getValue() == Double.parseDouble(array[1])) {
            			 result += entry.getKey()+", "+entry.getValue()+" KB \n";
                	 }
        		 }
        		 else if (array[2].equals("<")) {
        			 if (entry.getValue() == Double.parseDouble(array[1]) && entry.getValue() < Double.parseDouble(array[3])) {
            			 result += entry.getKey()+", "+entry.getValue()+" KB \n";
                	 }
        		 }
        		 else if (array[2].equals(">")) {
        			 if (entry.getValue() == Double.parseDouble(array[1]) && entry.getValue() > Double.parseDouble(array[3])) {
            			 result += entry.getKey()+", "+entry.getValue()+" KB \n";
                	 }
        		 }
        		 break;
        	 }
         }
         if (result.equals("")) {
        	 result = "Keine Ergebnisse gefunden";
         }
         return result;
	}
	
	private static String searchAllDocs(String searchString) {
		allDocsResult.clear();
		String[] searchArray = searchString.split("[\\s]+");
		int searchArrayLength = searchArray.length;
		String result = "Keine Ergebnisse\n";
		Set<Entry<String, String[]>> hashSet=allDocs.entrySet();
        for(Entry<String, String[]> entry:hashSet ) {
        	String[] array = entry.getValue();
    		Boolean oneWordFound = false;
    		Boolean bothWordsFound = false;
    		Boolean neededWordFound = false;
    		String otherWord = "";
        	for (int i=0; i<array.length; i++) {
	        	if (searchArrayLength == 1) {
	        		//suche nach 1 wort
	        		if (searchArray[0].equals(array[i])) {
	        			hashmContainKey(allDocsResult, entry.getKey());
	        			result = "";
	        		}
	        	}
	        	else if (searchArrayLength == 2) {
	        		//suche nach 2 aufeinanderfolgenden wörtern
	        		//ignoriere letztes element im array
	        		if (searchArray[1].equals("*")) {
	        			//findet unvollständige ähnliche wörter; suche nach student matcht auch studenten, studentinnen u.ä. 
	        			if (array[i].toLowerCase().contains(searchArray[0].toLowerCase())) {
		        			hashmContainKey(allDocsResult, entry.getKey());
		        			result = "";
		        		}
	        		}
	        		else {
	        			if (i+1<array.length && searchArray[0].equals(array[i]) && searchArray[1].equals(array[i+1])) {
		        			hashmContainKey(allDocsResult, entry.getKey());
		        			result = "";
		        		}
	        		}
	        	}
	        	else if (searchArrayLength == 3) {
	        		//suche mit wort1 oder wort2 bzw wort1 und wort2
	        		if (searchArray[1].equals("und")) {
	        			if(!oneWordFound && searchArray[0].equals(array[i])) {
	        				oneWordFound = true;
	        				otherWord = searchArray[2];
	        			}
	        			else if(!oneWordFound && searchArray[2].equals(array[i])) {
	        				oneWordFound = true;
	        				otherWord = searchArray[0];
	        			}
	        			else if (oneWordFound && !bothWordsFound && otherWord.equals(array[i])) {
	        				bothWordsFound = true;
	        				hashmContainKey(allDocsResult, entry.getKey());
		        			result = "";
	        			}
	        		}
	        		else if (searchArray[1].equals("oder")) {
	        			if (!oneWordFound && (searchArray[0].equals(array[i]) || searchArray[2].equals(array[i]))) {
	        				oneWordFound = true;
	        				hashmContainKey(allDocsResult, entry.getKey());
		        			result = "";
	        			}
	        		}
	        		else if (searchArray[1].equals("xor")) {
	        			if (!oneWordFound && searchArray[2].equals(array[i])) {
	        				oneWordFound = true;
	        			}
	        			else if (!oneWordFound && !neededWordFound && searchArray[0].equals(array[i])) {
	        				neededWordFound = true;
	        			}
	        			else if (!oneWordFound && neededWordFound && i==array.length-1) {
	        				hashmContainKey(allDocsResult, entry.getKey());
		        			result = "";
	        			}
	        		}
	        	}
        	}
        	if (searchArray[0].equals("levi")) {
        		result += "Datei: "+entry.getKey()+"\n";
        		int percentage = Integer.parseInt(searchArray[2]);
        		result += FuzzySearch.extractSorted(searchArray[1], Arrays.asList(array), percentage)+"\n";
    		}
        }
        allDocsResult = sortByValue(allDocsResult);
        return result;
	}
	
	private static Map<String, Integer> importantWordsGlossary() {
		importantWords = uniqueWordsOcc;
		Iterator<Map.Entry<String, Integer>> itr = importantWords.entrySet().iterator();
		while(itr.hasNext())
		{
		   Map.Entry<String, Integer> entry = itr.next();
		   if(stopWordsList.contains(entry.getKey().toLowerCase())) {
			   itr.remove();
		   }
		}
        importantWords = sortByValue(importantWords);
		return importantWords;
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	    return map.entrySet()
	    		.stream()
	    		.sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
	    		.collect(Collectors.toMap(
	    				Map.Entry::getKey,
	    				Map.Entry::getValue,
	    				(e1, e2) -> e1,
	    				LinkedHashMap::new
	    				));
	}
	
	public static <K, V extends Comparable<? super V>> Map<String, String[]> sortByValue2(Map<String, String[]> map) {
	    return map.entrySet()
	    		.stream()
	    		.sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
	    		.collect(Collectors.toMap(
	    				Map.Entry::getKey,
	    				Map.Entry::getValue,
	    				(e1, e2) -> e1,
	    				LinkedHashMap::new
	    				));
	}
	
	public static <K, V> Map<K, V> sortByKey(Map<K, V> map) {
		Map<K, V> treeMap = new TreeMap<K, V>(map);
		return treeMap;
	}
	
	public static void getBasicInfo() throws IOException{
		
		Files.walk(Paths.get(path)).filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".txt")).forEach(
				filePath ->{
				fileName = filePath.getFileName().toString();
				try {
					wordsArray = removeNull(readFile(fileName).split("[\\s,\\.]+"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				docCount++;
				try {
					double docSingleSize = Files.size(filePath);
					allDocSizes.put(path+"/"+fileName,docSingleSize/1000);
					docSize += docSingleSize;
				} catch (IOException e) {
					e.printStackTrace();
				}
				docSingleLength = wordsArray.length;
				docLength += docSingleLength;
				try {
					docChar += readFile(fileName).length();
				} catch (IOException e) {
					e.printStackTrace();
				}
				getContentDetails(wordsArray);
				getAllDocLength(fileName, docSingleLength);
				allDocs.put(path+"/"+fileName, wordsArray);
				//sort hashmaps descending according to value (biggest value first, smallest last):
				allDocSizes = sortByValue(allDocSizes);
				allDocLength = sortByValue(allDocLength);
			});
		
		String wordOcc = printHashMap(sortByValue(uniqueWordsOcc),"Wort","Häufigkeit");
		System.out.println("********** STATISTIK : **********");
		System.out.println("Anzahl Dokumente: "+docCount);
		System.out.println("Dokumentengrösse total: "+docSize/1000+" KB");
		System.out.println("Anzahl Wörter: "+docLength);
		System.out.println("Anzahl unterschiedliche Wörter: "+docUniqueWords);
		System.out.println("Durchschnittliche Anzahl Zeichen (inkl. Leerzeichen): "+docChar/docCount); //inkl. Leerzeichen!
		System.out.println("Häufigkeit aller Wörter: \n"+wordOcc);
		System.out.println("Anzahl Wörter jedes Dokuments: \n"+printHashMap(allDocLength,"Datei","Anzahl Wörter"));
		
		//word to search for needs to be defined at line 32
		System.out.println("Die Wortsuche ergab folgende Treffer: \n"+findWordInFileNot+printHashMap(wordWasFoundIn,"Format Datei:Keyword","Gefunden"));
		System.out.println("Glossary mit wichtigsten Wörtern: \n"+printHashMap(importantWordsGlossary(),"Datei","Anzahl Wörter"));
	}
	
	private static void commandoCenter() {
		while (true) {
			System.out.println("\n<------------------------------------------------------------>");
			System.out.println("Befehl eingeben: (\"help\" für Hilfe   |   \"exit\" um zu beenden)");
			Scanner scanInput = new Scanner(System.in);
			String userInput = scanInput.nextLine();
			switch(userInput) {
	         case "search" :
	        	 while (true) {
	        		 System.out.println("Beenden durch Eingabe von \"return\"");
	        		 System.out.println("Eingabe: Suchwort/-wörter/-phrase z.B. \"book\" oder \"the book\" (2 aufeinanderfolgende Wörter) oder \"the und book\" (beide müssen vorkommen; und ist Schlüsselwort)");
	        		 System.out.println("oder \"the oder book\" (eins von beiden muss vorkommen; oder ist Schlüsselwort) oder \"the xor book\" (book darf NICHT vorkommen; xor ist Schlüsselwort)");
	        		 System.out.println("oder \"student *\" (matcht auch Studenten; unvollständige Wörter unabhängig von Gross-/Kleinschreibung)");
	        		 System.out.println("oder \"levi boo 80\" (benutzt Levenshtein-Distanz, um boo mit min. 80% Übereinstimmung zu finden)");
	        		 String userInput2 = scanInput.nextLine();
	        		 if (userInput2.equals("return")) {
	        			 break;
	        		 }
	        		 System.out.print(searchAllDocs(userInput2));
		        	 System.out.println(printHashMap(allDocsResult,"Datei","Gefunden"));
	        	 }	        	
	            break;
	         case "size" :
	        	 while (true) {
	        		 System.out.println("Beenden durch Eingabe von \"return\"");
	        		 System.out.println("Eingabe: \"< 5.0\" oder \"> 1.25\" oder \"= 0.111\" oder Kombination z.B. < 1 > 0.1 | Zahl in KiloBytes als double");
	        		 String userInput3 = scanInput.nextLine();
	        		 if (userInput3.equals("return")) {
	        			 break;
	        		 }
	        		 System.out.println(allDocSizesCompare(userInput3)+"\n");
	        	 }
	         case "help" :
	            System.out.println("Folgende Befehle sind verfügbar:");
	            System.out.println("-search: Gibt aus wo (in welchen Dateien) und wie häufig das Suchwort vorkommt. Suche mit Levenshtein-Distanz möglich");
	            System.out.println("-size: Gibt alle Dateien aus oder nur diejenigen welche der Eingabe <, >, = Zahl (KB als double) \noder einer Kombination davon entsprechen");
	            break;
	         case "exit" :
	        	scanInput.close();
				System.out.println("Beendet");
				return;
	         default :
	            System.out.println("Ungültige Eingabe. Bitte versuchen Sie es erneut.");
	            break;
			}
		}
	}
	
}
