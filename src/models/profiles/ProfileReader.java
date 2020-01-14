package models.profiles;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import models.test.Response;
import models.test.results.GrammarResult;
import models.test.results.GrammarStage;
import models.test.results.GrammarStructure;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ProfileReader {
	public final static String PROFILE_PATH = "./src/resources/profiles/";

	public static Profile readProfileFromXML(File xml) {
		HashMap<String, String> info = new HashMap<>();
		List<String> testAges = new LinkedList<>();
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(xml);
			Element root = document.getRootElement();
			// get attribute info
			for(Attribute attribute : root.attributes()){
				info.put(attribute.getName(), attribute.getValue());
			}
			info.put("profileName", xml.getName().split("\\.")[0]);

			// read test ages
			Iterator rootElements = root.elementIterator();
			while (rootElements.hasNext()) {
				Element rootElement = (Element) rootElements.next();
				Iterator testElements = rootElement.elementIterator();
				while (testElements.hasNext()) {
					Element test = (Element) testElements.next();
					String age = test.attribute("age").getValue();
					if (!testAges.contains(age))
						testAges.add(age);
				}
			}
			info.put("ages", String.join(",", testAges));

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return new Profile(info);
	}

	public static ArrayList<GrammarResult> readGrammarResultsFromXML(String filename) {
		ArrayList<GrammarResult> results = new ArrayList<>();
		SAXReader reader = new SAXReader();
		try {
			File xml = new File(PROFILE_PATH + filename + ".xml");

			Document document = reader.read(xml);
			Element root = document.getRootElement();

			Iterator rootElements = root.elementIterator();
			while (rootElements.hasNext()) {
				Element rootElement = (Element) rootElements.next();

				if (rootElement.getName().equalsIgnoreCase("grammar")) {

					Iterator testElements = rootElement.elementIterator();
					while (testElements.hasNext()) {
						Element test = (Element) testElements.next();

						List<GrammarStage> stageResults = new LinkedList<>();
						SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String testTime = test.attribute("time").getValue();
						String testAge = test.attribute("age").getValue();
						String testScore = test.attribute("score").getValue();

						Iterator stageElements = test.elementIterator();
						while (stageElements.hasNext()) {
							Element stage = (Element) stageElements.next();

							String stageNo = stage.attribute("stage_no").getValue();
							String stageScore = stage.attribute("stage_score").getValue();
							GrammarStage grammarStage = new GrammarStage(Integer.parseInt(stageNo));
							grammarStage.setStageScore(Double.parseDouble(stageScore));

							Iterator questionElements = stage.elementIterator();
							while (questionElements.hasNext()) {
								Element question = (Element) questionElements.next();

								String target = question.attribute("name").getValue();
								String score = question.attribute("score").getValue();

								Iterator responseElements = question.elementIterator();
								while (responseElements.hasNext()) {
									Element response = (Element) responseElements.next();
									grammarStage.addRecord(new Response(response.getStringValue()), new GrammarStructure(target, Integer.parseInt(score)));
								}
							}
							stageResults.add(grammarStage);
						}
						results.add(new GrammarResult(stageResults, f.parse(testTime, new ParsePosition(0)), testAge, testScore));
					}
				} else if (rootElement.getName().equalsIgnoreCase("pronun")) {
					// todo read pronun results
				}

			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		System.out.println(results.size());
		return results;
	}

}
