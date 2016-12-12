package fi.holti.jyu.vr.service.impl;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import fi.holti.jyu.vr.model.Train;

@RunWith(MockitoJUnitRunner.class)
public class TrainServiceImplTest {

	@InjectMocks
	private TrainServiceImpl trainServiceImpl;

	@Mock
	private RestTemplate restTemplate;

	@Test
	public void skipsEverything_WithZeroTrainNumbers() {
		trainServiceImpl.getTrains(new ArrayList(), new Date(), new Date());
		verifyZeroInteractions(restTemplate);
	}

	private void assertCorrectUrl(String url) {

	}

	@Test
	public void executesRestTemplateGotForObject_WithOneTrainNumber() {
		List<String> trainNumbers = new ArrayList<String>();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(2016, Calendar.JANUARY, 1, 0, 0);
		Date start = calendar.getTime();

		calendar.set(2016, Calendar.FEBRUARY, 1, 0, 0);

		Date end = calendar.getTime();

		Train[] trains = new Train[5];

		Train train = new Train();

		trains[0] = train;

		when(restTemplate.getForObject(eq(""), eq(Train[].class))).thenReturn(trains);

		trainServiceImpl.getTrains(trainNumbers, start, end);

		// verify(restTemplate).getForObject(eq(""), eq(Train[].class));
	}

	@Test
	public void executesRestTemplateGotForObject_WithMultipleTrainNumbers() {

	}
}
