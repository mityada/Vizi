import org.w3c.dom.*;
import java.util.*;

public class ViziTools {
    public static String convert(
        String statement,
        String proc,
        NodeList globalVariables,
        NodeList localVariables
    ) throws Exception {
        try {
        HashSet global = toSet(globalVariables);
        HashSet local = toSet(localVariables);
        StringBuffer result = new StringBuffer();
        for (int pos = 0; pos < statement.length(); pos++) {
            if (statement.charAt(pos) == '@') {
                if (statement.length() > pos + 1 && 
                    Character.isJavaIdentifierStart(statement.charAt(pos + 1))
                ) {
                    String id = getId(statement.substring(pos + 1));
                    pos += id.length();
                    if (local.contains(id)) {
                        result.append("d.").append(proc).append('_').append(id);
                    } else if (global.contains(id)) {
                        result.append("d.").append(id);
                    } else {
                        if (statement.length() > pos + 2 && 
                            statement.charAt(pos + 1) == '@' && 
                            Character.isJavaIdentifierStart(statement.charAt(pos + 2))
                        ) {                            
                            result.append("d.").append(id).append('_');
                            id = getId(statement.substring(pos + 1));
                            pos += id.length() + 1;
                            result.append(id);
                        }
                    }
                } else {
                    result.append('@');
                }
            } else {
                result.append(statement.charAt(pos));
            }
        }
        return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String getId(String s) {
        int i;
        for (i = 0; i < s.length(); i++) {
            if (!Character.isJavaIdentifierPart(s.charAt(i))) {
                break;
            }
        }
        return s.substring(0, i);
    }

    public static HashSet toSet(NodeList list) {
        HashSet result = new HashSet();
        if (list != null) {
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i).getAttributes().getNamedItem("name");
                if (node != null) {
                    String name = node.getNodeValue();
                    if (name != null) {
                        result.add(name);
                    }
                }
            }
        }
        return result;
    }
}

