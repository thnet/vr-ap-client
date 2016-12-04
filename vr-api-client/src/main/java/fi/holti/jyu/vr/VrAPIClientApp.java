package fi.holti.jyu.vr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import fi.holti.jyu.vr.service.impl.VRAPIClient;

/**
 * Solution for querying VR API. We need certain trains timetables from two
 * years date range as json file
 * 
 * @author timoh
 *
 */
@SpringBootApplication
public class VrAPIClientApp {

	public static void main(String args[]) {
		ApplicationContext applicationContext = SpringApplication.run(VrAPIClientApp.class, args);
		VRAPIClient vrapiClient = applicationContext.getBean(VRAPIClient.class);
		vrapiClient.run();
	}

}
