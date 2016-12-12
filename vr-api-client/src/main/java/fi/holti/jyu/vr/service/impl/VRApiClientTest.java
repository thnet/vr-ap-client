package fi.holti.jyu.vr.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.logging.LogFactory;

public class VRApiClientTest {
	private static final org.apache.commons.logging.Log log = LogFactory.getLog(VRAPIClient.class);

	public void readNumbers() {
		List<String> trainNumbers = new ArrayList<String>();

		// Java 7 NIO API
		try (Stream<String> stream = Files
				.lines(Paths.get(getClass().getResource("trainnumbers.properties").toURI()))) {

			trainNumbers = stream.collect(Collectors.toList());
			log.info("Found " + trainNumbers.size() + " trainNumbers for properties");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		// Java 8 Lambda features used for moving possible duplicates
		List<String> uniqueTrainNumbers = trainNumbers.parallelStream().distinct().collect(Collectors.toList());

		log.info("Found " + uniqueTrainNumbers.size() + " unique trainNumbers");
		Map<Integer, List<String>> trainNumbersByThreadNumber = new TreeMap<Integer, List<String>>();

		trainNumbersByThreadNumber.put(1, uniqueTrainNumbers);

		mapTrainNumbersIntoSameSizeSublists(trainNumbers, trainNumbersByThreadNumber);
	}

	private void mapTrainNumbersIntoSameSizeSublists(List<String> trainNumbers,
			Map<Integer, List<String>> trainNumbersByThreadNumber) {

		// Assume that there is 48 items. Split that into sublists 8 items per
		// list
		int lastModulo6Index = 40;
		for (int i = 0; i <= lastModulo6Index; i = i + 8) {
			int inclusiveIndex = i;
			int exlusiveIndex = i + 8;
			List<String> subList = trainNumbers.subList(inclusiveIndex, exlusiveIndex);
			System.out.println("Put: i=" + i + " subList=" + subList);
			trainNumbersByThreadNumber.put(i, subList);
		}
	}

}
