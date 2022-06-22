package common.util;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JavaScript {
	
	public static ScriptEngineManager mgr = new ScriptEngineManager();
	public static ScriptEngine engine = mgr.getEngineByName("JavaScript");
	
	public static Object eval(String formula, String value) throws ScriptException {
		
		String operation = formula.toLowerCase().trim();
		operation = operation.replace("0x", "H");
		operation = operation.replace("x", value).replace("and", "&&").replace("or", "||");		
		operation = operation.replace("H", "0x");

		return engine.eval(operation);
	}
}
