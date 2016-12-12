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
import fi.holti.jyu.vr.model.Cause;
import fi.holti.jyu.vr.model.TimeTableRow;
import fi.holti.jyu.vr.model.Train;
import fi.holti.jyu.vr.model.simple.SimpleTimetableRow;
import fi.holti.jyu.vr.model.simple.SimpleTrain;
import fi.holti.jyu.vr.service.TrainService;

@Component
public class VRAPIClient implements VrAPIClientInterface {

	private static final org.apache.commons.logging.Log log = LogFactory.getLog(VRAPIClient.class);
	@Autowired
	private TrainService trainService;
	private static final String[] END_STATIONS = { "HKI", "TKU", "TPE" };
	private static Map<String, String> weatherLocationCodesByEndStationCode = new TreeMap<String, String>();
	private static final String HELSINKI_END_STATION = "HKI";
	private static final String TURKU_END_STATION = "TKU";
	private static final String TPE_END_STATION = "TPE";
	private static final String HELSINKI_KAISANIEMI = "100971";
	private static final String TURKU_ARTUKAINEN = "100949";
	private static final String TAMPERE_HARMALA = "101124";

	static {
		weatherLocationCodesByEndStationCode.put(TPE_END_STATION, HELSINKI_KAISANIEMI);
		weatherLocationCodesByEndStationCode.put(TURKU_END_STATION, HELSINKI_KAISANIEMI);
		weatherLocationCodesByEndStationCode.put(HELSINKI_END_STATION + "<800", TAMPERE_HARMALA);
		weatherLocationCodesByEndStationCode.put(HELSINKI_END_STATION + ">800", TURKU_ARTUKAINEN);
	}

	public void run() {

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

		mapTrainNumbersIntoSameSizeSublists(trainNumbers, trainNumbersByThreadNumber);

		log.info(trainNumbersByThreadNumber.values());

		// Convert train object into lists of simpler, non-nested
		// objects. Target system, the BigInsights does not support
		// nested JSON.
		List<SimpleTrain> simpleTrains = new ArrayList<SimpleTrain>();
		List<SimpleTimetableRow> endStationTimetables = new ArrayList<SimpleTimetableRow>();
		List<Cause> causes = new ArrayList<Cause>();

		List<Thread> startedThreads = new ArrayList<Thread>();

		for (List<String> trainNumbersForThreadExecution : trainNumbersByThreadNumber.values()) {
			// A bit too complex, could be refactored...
			Thread thread = new Thread() {
				@Override
				public void run() {
					super.run();
					log.info("Executing thread id=" + this.getId() + " with trainNumbersForThreadExecution="
							+ trainNumbersForThreadExecution);

					Calendar calendar = Calendar.getInstance();

					// 10.8.2015-1.2.2016

					// temp
					calendar.set(2015, Calendar.JANUARY, 1, 00, 00);
					Date start = calendar.getTime();
					Date end = new Date();
					List<Train> trains = trainService.getTrains(trainNumbersForThreadExecution, start, end);

					for (Train train : trains) {

						SimpleTrain simpleTrain = new SimpleTrain(train.getTrainNumber(), train.getDepartureDate(),
								train.getTrainType(), train.getTrainCategory(), train.getScheduledTime(),
								train.getDifferenceInMinutes());

						simpleTrains.add(simpleTrain);

						int index = 0;
						for (TimeTableRow timeTableRow : train.getTimeTableRows()) {
							SimpleTimetableRow simpleTimetableRow = new SimpleTimetableRow(train.getTrainNumber(),
									train.getDepartureDate(), timeTableRow.getStationShortCode(), train.getTrainType(),
									timeTableRow.getScheduledTime(), timeTableRow.getActualTime(),
									timeTableRow.getDifferenceInMinutes());

							String stationShortCode = simpleTimetableRow.getStationsShortCode();
							if (isInterestingEndStation(stationShortCode) && !isStartingsStation(index)) {

								if (stationShortCode.equals(TURKU_END_STATION)
										|| stationShortCode.equals(TPE_END_STATION)) {
									simpleTimetableRow.setWeatherLocationId(
											weatherLocationCodesByEndStationCode.get(stationShortCode));
								} else if (stationShortCode.equals(HELSINKI_END_STATION)
										&& Integer.parseInt(train.getTrainNumber()) < 800) {
									simpleTimetableRow.setWeatherLocationId(
											weatherLocationCodesByEndStationCode.get(stationShortCode + "<800"));
								} else if (stationShortCode.equals(HELSINKI_END_STATION)
										&& Integer.parseInt(train.getTrainNumber()) > 800) {
									simpleTimetableRow.setWeatherLocationId(
											weatherLocationCodesByEndStationCode.get(stationShortCode + ">800"));
								}

								endStationTimetables.add(simpleTimetableRow);

								for (Cause cause : timeTableRow.getCauses()) {
									cause.setStationsShortCode(stationShortCode);
									cause.setTrainNumber(train.getTrainNumber());
									cause.setDepartureDate(train.getDepartureDate());
									causes.add(cause);
								}
							}
							index++;

						}

					}

				}

				private boolean isStartingsStation(int index) {
					return index == 0;
				}

				private boolean isInterestingEndStation(String stationsShortCode) {
					boolean isInteresting = false;
					for (String stationCode : END_STATIONS) {
						isInteresting = stationCode.equals(stationsShortCode);
						if (isInteresting) {
							break;
						}
					}
					return isInteresting;
				}

			};
			thread.start();

			startedThreads.add(thread);

		}

		// Wait until each thread dies. This is old way, you could use newer
		// features from java.util.concurrent.

		for (Thread thread : startedThreads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		String fileName = "trainschedules-numbers.json";

		JSONFileWriter.writeObjectsAsJSONFile(simpleTrains, fileName);
		fileName = "trainschedules-timetablerows.json";
		JSONFileWriter.writeObjectsAsJSONFile(endStationTimetables, fileName);

		fileName = "trainschedules-causes.json";

		JSONFileWriter.writeObjectsAsJSONFile(causes, fileName);

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
