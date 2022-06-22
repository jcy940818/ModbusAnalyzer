package common.util;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JavaScript {
	public static Object eval(String formula, String value) throws ScriptException {
		
		String operation = formula.toLowerCase().trim();
		operation = operation.replace("0x", "H");
		operation = operation.replace("x", value).replace("and", "&&").replace("or", "||");		
		operation = operation.replace("H", "0x");
		
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");

		return engine.eval(operation);
	}
}
