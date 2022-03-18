package common.perf;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

public class XmlUtils {
	public static XMLConfiguration createXMLConfiguration(String fileName) throws ConfigurationException {
		XMLConfiguration config = new XMLConfiguration();
		config.setDelimiterParsingDisabled(true);
		// Attribute를 |를 구분자로 List로 변환하는 것 방지
		config.setAttributeSplittingDisabled(true);
		config.setFileName(fileName);
		config.setReloadingStrategy(new FileChangedReloadingStrategy());
		config.load();

		return config;
	}
}
