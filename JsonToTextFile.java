/**
 * 
 */
package com.pmk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.text.StrSubstitutor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

/**
 * @author manteshkumar.p
 *
 */
public class JsonToTextFile {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException, ParseException {
		String jsonData = readFile("properties.json"); 
		JSONParser parser = new JSONParser(); 
		JSONObject json = (JSONObject) parser.parse(jsonData);
		Collection<JSONArray> hashMap =  json.values();
		Iterator<JSONArray> iterator = hashMap.iterator();
		Set<String> geneName = new HashSet<>();
		while(iterator.hasNext()) {
			JSONArray jsonArray = iterator.next();
			Iterator<JSONObject> iterator2 = jsonArray.iterator();
			while(iterator2.hasNext()) {
				JSONObject array = iterator2.next();
				String sampleName = ((String) array.get("sample")).replace(" ", "_");
				JSONObject jsonObject = (JSONObject) array.get("mapOfTargets");
				Set<Entry<String, JSONObject>> entries = jsonObject.entrySet();
				String fileName = "/Users/manteshkumar.p/Documents/txtFile_Location/Panel"+sampleName+".txt";
				int index = 0;
				StringBuilder builder = new StringBuilder();
				for(Entry<String, JSONObject> entry :  entries) {
					if(index == 0) {
						builder.append("gene").append("\t").append("sigal").append("\n");
					} else {
						HashMap<String, Double> hashMap2 = (HashMap<String, Double>) entry.getValue();
						builder.append(entry.getKey()).append("\t").append(hashMap2.get("valueTarget")).append("\n");
						geneName.add(entry.getKey());
					}
					index++;
				}
				System.out.println("File name is ==="+fileName);
				try (Writer writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(fileName), "utf-8"))) {
					writer.write(builder.toString());
				}
			}
		}
		generateCSVTemplate(geneName);
	}

	public static void generateCSVTemplate(Set<String> geneName) {
		try { 
			FileReader filereader = new FileReader("/Users/manteshkumar.p/Documents/txtFile_Location/AnnotationTemplate.csv"); 
			CSVReader csvReader = new CSVReaderBuilder(filereader) 
					.withSkipLines(0) 
					.build(); 
			List<String[]> allData = csvReader.readAll(); 
			csvReader.close();
			List<String[]> newData = new ArrayList<>();
			int index = 0;
			for (String[] row : allData) { 
				if(index == 2) {
					Map<String,String> map = new HashMap<String, String>();
					map.put("panel", "Panel");
					row[0] = StrSubstitutor.replace(row[0],map,"{","}");
				} else if(index == allData.size() - 1){
					int geneIndex = 0;
					for(String gene : geneName) {
						if(geneIndex == 0) {
							row[0] = gene;
							row[14] = gene;
						} else {
							String[] copyArray = Arrays.copyOf(row, row.length);
							copyArray[0] = gene;
							copyArray[14] = gene;
							newData.add(copyArray);
						}
						geneIndex++;
					}
				} 
				index++;
			} 
			allData.addAll(newData);

			String csv = "/Users/manteshkumar.p/Documents/txtFile_Location/Panel1.annot.csv";
			CSVWriter writer = new CSVWriter(new FileWriter(csv));

			//Write the record to file
			writer.writeAll(allData);

			//close the writer
			writer.close();
		} 
		catch (Exception e) { 
			e.printStackTrace(); 
		} 
	}

	public static String readFile(String filename) throws IOException {
		String result = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			result = sb.toString();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			br.close();
		}
		return result;
	}
}
