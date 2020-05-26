package com.salesboxai.zoom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/*		outcome/
 * We store a POJO as a Newline-delimitied JSON (http://ndjson.org/)
 * - each version as a new line.
 */
public class SimplePersist {

	private String dbName;
	private boolean hasdata;

	public SimplePersist(String dbName) throws IOException {
		this.dbName = dbName;
		try {
			BufferedReader db = new BufferedReader(new FileReader(dbName));
			hasdata = db.readLine() != null;
			db.close();
		} catch(FileNotFoundException e) {
			hasdata = false;
		}
	}

	public <T> void save(T pojo) throws Exception {
		BufferedWriter db = new BufferedWriter(new FileWriter(dbName, true));
		ObjectMapper mapper = new ObjectMapper();
		String ndjson = mapper.writeValueAsString(pojo);
		if(hasdata) ndjson = "\n" + ndjson;
		db.write(ndjson);
		db.flush();
		db.close();
	}

	public <T> T load(Class<T> cls) throws IOException {
		String line, last = null;
		try {
			BufferedReader db = new BufferedReader(new FileReader(dbName));
			while((line  = db.readLine()) != null) {
				last = line;
			}
			db.close();
		} catch(FileNotFoundException e) {}
		if(last == null) return null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(last, cls);
	}
}