package fi.holti.jyu.vr.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class JSONFileWriter {
	private static final Logger logger = LoggerFactory.getLogger(JSONFileWriter.class);

	// prevent instantiation
	private JSONFileWriter() {
	}

	public static void writeObjectsAsJSONFile(List<?> objects, String fileName) {
		try {

			logger.info("Writing json objects as file, amount=" + objects.size() + " fileName=" + fileName);

			// Google JSON
			Gson gson = new Gson();
			// Writer for character stream writing
			FileWriter fileWriter = new FileWriter(fileName);
			fileWriter.write(gson.toJson(objects));
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
