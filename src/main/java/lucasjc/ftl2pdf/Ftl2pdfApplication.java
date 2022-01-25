package lucasjc.ftl2pdf;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class Ftl2pdfApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Ftl2pdfApplication.class, args);
	}

	private FTLProcessor ftlProc;

	private PDFGenerator pdfGen;

	/**
	 * @param ftlProc
	 * @param pdfGen
	 */
	public Ftl2pdfApplication(FTLProcessor ftlProc, PDFGenerator pdfGen) {
		this.ftlProc = ftlProc;
		this.pdfGen = pdfGen;
	}

	@Override
	public void run(String... args) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("message", "Hello!");

		List<Integer> elements = new Random().ints(50).boxed().collect(Collectors.toList());
		params.put("elements", elements);

		String html = ftlProc.process("example.ftl", params);
		byte[] pdf = pdfGen.html2pdf(html);

		File outputFile = new File("output.pdf");
		try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
			outputStream.write(pdf);
		}
	}
}
