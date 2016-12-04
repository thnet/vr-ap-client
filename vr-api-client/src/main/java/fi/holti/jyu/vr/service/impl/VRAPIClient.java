package fi.holti.jyu.vr.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.holti.jyu.vr.VrAPIClientInterface;
import fi.holti.jyu.vr.io.JSONFileWriter;
import fi.holti.jyu.vr.service.TrainService;

@Component
public class VRAPIClient implements VrAPIClientInterface {

	private static final org.apache.commons.logging.Log log = LogFactory.getLog(VRAPIClient.class);
	@Autowired
	private TrainService trainService;

	public void run() {

		List<String> trainNumbers = new ArrayList<String>();

		// Java 7 NIO API
		try (Stream<String> stream = Files
				.lines(Paths.get(getClass().getResource("trainNumbers.properties").toURI()))) {

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

		mapTrainNumbersIntoSameSizeSublists(trainNumbers, trainNumbersByThreadNumber);

		log.info(trainNumbersByThreadNumber.values());

		for (List<String> trainNumbersForThreadExecution : trainNumbersByThreadNumber.values()) {
			Thread thread = new Thread() {
				@Override
				public void run() {
					super.run();
					log.info("Executing thread id=" + this.getId());

					Calendar calendar = Calendar.getInstance();
					// temp
					calendar.set(2016, Calendar.JANUARY, 1, 00, 00);
					Date start = calendar.getTime();
					// Until current date
					// calendar.set(2015, Calendar.DECEMBER, 31, 23, 0);
					Date end = new Date();

					String fileName = "trainschedules-numbers-" + this.getId() + calendar.get(Calendar.YEAR) + ".json";
					JSONFileWriter.writeObjectsAsJSONFile(
							trainService.getTrains(trainNumbersForThreadExecution, start, end), fileName);
				}

			};
			thread.start();

		}

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
			trainNumbersByThreadNumber.put(i, subList);
		}
	}

}
