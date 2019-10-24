package it.prg;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import it.prg.util.Utils;

@Service
public class XmlImporterImpl {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Jaxb2Marshaller marshaller;
	
	private Map<Path, List<String>> filesMap = new HashMap<>();
	private Map<String, List<String>> filesStatusMap = new HashMap<>();
	
	private static final String TO_PROCESS = "TO_PROCESS";
	private static final String PROCESSED = "PROCESSED";
	private static final String FAILED = "FAILED";
	
	@PostConstruct
	private void initializeStatusMap() {
		filesStatusMap.put(TO_PROCESS, new ArrayList<>());
		filesStatusMap.put(PROCESSED, new ArrayList<>());
		filesStatusMap.put(FAILED, new ArrayList<>());
	}
	
	public void processFile(String fileName) throws JAXBException, IOException {
		List<String> files = new ArrayList<>();
		filesMap = Utils.readFilesFromPath(Paths.get(fileName));

		for(Entry<Path, List<String>> entry : filesMap.entrySet()) {
			unmarshalXmlPerDirectory(entry.getKey().toString(), entry.getValue());
		}
		
	}
	
	//change to Map if required to know the dir
	public void handleProccessed(Object obj, String dir, String file) {
		String methodName = "handleProccessed()";
		logger.info("{} unmarshalling file [file={}] finished.", methodName, file);
		filesStatusMap.get(PROCESSED).add(file);
		
		//map the object to an entity or do something with it
	}

	public void handleFailed(String dir, String file) {
		String methodName = "handleFailed()";
		logger.error("{} unmarshalling file [file={}] failed.", methodName, file);
		filesStatusMap.get(FAILED).add(file);
	}

	private void unmarshalXmlPerDirectory(String dir, List<String> files) {
		String methodName = "unmarshalXmlPerDirectory()";
		logger.info("{} method Start.", methodName);
		String currentFile = null;
		
		try {
			for (String file : files) {
				currentFile = file;
				// unmarshaling code here
				Object obj = marshaller.unmarshal(new StreamSource(file));
				handleProccessed(obj, dir, currentFile);
			}

		} catch (Exception e) {
			logger.error("Exception occured: {} ", e.getMessage());
			logger.error("Exception occured: {} ", e);
			handleFailed(dir, currentFile);
		}finally {
			logger.info("{} method end.", methodName);
		}
	}

}
