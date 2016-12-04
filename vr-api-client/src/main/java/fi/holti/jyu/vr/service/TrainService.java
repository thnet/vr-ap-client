package fi.holti.jyu.vr.service;

import java.util.Date;
import java.util.List;

import fi.holti.jyu.vr.model.Train;

public interface TrainService {

	List<Train> getTrains(List<String> trainNumbers, Date startDate, Date endDate);
}
