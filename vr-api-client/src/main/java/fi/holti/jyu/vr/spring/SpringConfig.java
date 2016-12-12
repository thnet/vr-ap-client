package fi.holti.jyu.vr.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import fi.holti.jyu.vr.service.TrainService;
import fi.holti.jyu.vr.service.impl.TrainServiceImpl;

/**
 * Spring JavaConfig class
 * 
 * @author timoh
 *
 */
@Configuration
public class SpringConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public TrainService trainService() {
		return new TrainServiceImpl();
	}
}
