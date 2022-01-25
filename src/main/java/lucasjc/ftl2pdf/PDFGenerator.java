package lucasjc.ftl2pdf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PDFGenerator {

    private String resourcesPath;

    /**
     * @param resourcesPath
     */
    public PDFGenerator(@Value("${pdf.resources-path:classpath:templates/}") String resourcesPath) {
        this.resourcesPath = resourcesPath;
    }

    /**
     * @param html
     * @return PDF byte array for given html
     * @throws IOException
     */
    public byte[] html2pdf(String html) throws IOException {
        try (ByteArrayOutputStream pdfOS = new ByteArrayOutputStream()) {

            // 300DPI https://stackoverflow.com/questions/20495092/flying-saucer-set-custom-dpi-for-output-pdf
            ITextRenderer renderer = new ITextRenderer(4.1666f, 3);

            FlyingSaucerCustomResourceLoader loader = new FlyingSaucerCustomResourceLoader(renderer.getOutputDevice(), this.resourcesPath);
            loader.setSharedContext(renderer.getSharedContext());

            renderer.getSharedContext().setUserAgentCallback(loader);
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(pdfOS);

            return pdfOS.toByteArray();
        }
    }
}
