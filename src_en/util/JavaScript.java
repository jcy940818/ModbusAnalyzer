package src_en.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JavaScript {
	public static Object eval(String scaleFunction, String value) throws ScriptException {
				
		if (value.contains("0x") ||
			value.contains("Unknown") ||
			value.contains("Exception") || 
			value.contains("Response Timeout") ||
			value.contains("CRC Error") || 
			value.contains("---")) {
			return false;
		}
		
		String operation = scaleFunction.toLowerCase();
		operation = operation.replace("x", value).replace("and", "&&").replace("or", "||");		
		
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");

		return engine.eval(operation);
	}
}
