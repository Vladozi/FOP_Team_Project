package FOP_Team_Project;

import java.util.Map;
public class Methods {
    // Method to print the value of a variable
    public static Object printVariable(String varName, Map<String, Object> variables) {
        Object value = variables.get(varName);
        if (value != null) {
            return value; // Return the value to be printed
        } else {
            return "Error: Variable '" + varName + "' is not defined.";
        }
    }
}