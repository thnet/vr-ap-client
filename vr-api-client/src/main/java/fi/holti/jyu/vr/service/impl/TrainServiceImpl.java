package fi.holti.jyu.vr.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import fi.holti.jyu.vr.model.Train;
import fi.holti.jyu.vr.service.TrainService;

@Service
public class TrainServiceImpl implements TrainService {
	@Autowired
	private RestTemplate restTemplate;
	private String VR_API_URL = "https://rata.digitraffic.fi/api/v1/history/";
	private static final Logger log = LoggerFactory.getLogger(TrainServiceImpl.class);

	public List<Train> getTrains(List<String> trainNumbers, Date startDate, Date endDate) {

		log.info("Getting trains by train numbers= " + trainNumbers + " startDate=" + startDate + " , endDate="
				+ endDate);

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(startDate);
		List<Train> allTrains = new ArrayList<Train>();
		calendar.setTime(startDate);

		Map<String, String> failedRequests = new LinkedHashMap<String, String>();

		for (String trainNumber : trainNumbers) {

			while (calendar.getTime().before(endDate)) {

				if (isDayForPrediction(calendar.get(Calendar.DAY_OF_WEEK))) {

					String url = parseREstUrl(calendar, trainNumber);
					try {
						List<Train> trains = Arrays.asList(restTemplate.getForObject(url, Train[].class));
						allTrains.addAll(trains);
					} catch (HttpMessageNotReadableException e) {
						// Try to get exception response as a string
						try {
							String jsonResponse = restTemplate.getForObject(url, String.class);
							failedRequests.put(url, jsonResponse);
						} catch (Exception e2) {
							// if even that fails, backup it
							failedRequests.put(url, e2.getMessage());
						}
					}
				}
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			calendar.setTime(startDate);

		}
		log.info("Found " + allTrains.size() + " train schedules");

		for (String url : failedRequests.keySet()) {
			String exception = failedRequests.get(url);

			log.info("Failed request, url= " + url + " exception=" + exception);
		}

		return allTrains;

	}

	private boolean isDayForPrediction(int i) {
		return Calendar.WEDNESDAY == i;
	}

	private String parseREstUrl(Calendar calendar, String trainNumber) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(VR_API_URL);
		stringBuilder.append(trainNumber);
		stringBuilder.append("?");
		stringBuilder.append("departure_date=");
		String simpleDateSring = toDateString(calendar.getTime());
		stringBuilder.append(simpleDateSring);

		String url = stringBuilder.toString();
		return url;
	}

	private String toDateString(Date time) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(time);
	}

}
