package lucasjc.ftl2pdf;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextUserAgent;

/**
 * Custom resource loader that correctly resolves paths for using with flying-saucer
 */
public class FlyingSaucerCustomResourceLoader extends ITextUserAgent {

	private static final Logger LOG = LoggerFactory.getLogger(FlyingSaucerCustomResourceLoader.class);

	private String resourcesPath;
	
	/**
	 * 
	 * @param outputDevice
	 * @param resourcesPath
	 */
	public FlyingSaucerCustomResourceLoader(ITextOutputDevice outputDevice, String resourcesPath) {
		super(outputDevice);
		this.resourcesPath = resourcesPath;
	}

	/**
	 * the magic
	 */
	@Override
    protected InputStream resolveAndOpenStream(String uri) {

        InputStream is = super.resolveAndOpenStream(uri);
        String fileName = "";
        
        try {
            String[] split = uri.split("/");
            fileName = split[split.length - 1];
        } catch (PatternSyntaxException e) {
            throw new IllegalStateException(e);
        }
        
        if (is == null) {
        	PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        	Resource r = resolver.getResource(resourcesPath + "/" + fileName);
        	if(r.exists()) {
        		try {
					is = r.getInputStream();
				} catch (IOException e) {
					LOG.warn("Error loading resource {}/{}", resourcesPath, fileName, e);
				}
        	} else {
        		LOG.error("Resource not available {}/{}", resourcesPath, fileName);
        	}
        }

        return is;
    }
}