package common.perf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

public class XmlUtils {
	public static XMLConfiguration createXMLConfiguration(String fileName, String encoding) throws ConfigurationException, FileNotFoundException {
		
		File file = new File(fileName);
		
		XMLConfiguration config = new XMLConfiguration();
		config.setDelimiterParsingDisabled(true);
		// Attribute�� |�� �����ڷ� List�� ��ȯ�ϴ� �� ����
		config.setAttributeSplittingDisabled(true);
		config.setFile(file);
		config.setReloadingStrategy(new FileChangedReloadingStrategy());
		config.load(new FileInputStream(file), encoding);

		return config;
	}
}
