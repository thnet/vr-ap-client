package fi.holti.jyu.vr.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;

import fi.holti.jyu.vr.model.Train;

public class JSONFileWriter {

	// prevent instantiation
	private JSONFileWriter() {
	}

	public static void writeObjectsAsJSONFile(List<Train> trains, String fileName) {
		try {
			// Google JSON
			Gson gson = new Gson();
			// Writer for character stream writing
			FileWriter fileWriter = new FileWriter(fileName);
			fileWriter.write(gson.toJson(trains));
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
