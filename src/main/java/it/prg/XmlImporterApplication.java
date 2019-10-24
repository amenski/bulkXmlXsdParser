package it.prg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = { "it.prg" })
public class XmlImporterApplication implements CommandLineRunner {

	@Autowired
	private XmlImporterImpl importService;

	public static void main(String[] args) {
		new SpringApplication(XmlImporterApplication.class).run(args);
	}

	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("it.prg.dto");
		marshaller.setSchema(new ClassPathResource("/xsd/sample.xsd"));
		return marshaller;
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			if (args == null || args.length < 1)
				throw new RuntimeException("Please insert file location.");

			importService.processFile(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
