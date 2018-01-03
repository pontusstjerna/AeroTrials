package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by pontu on 2016-04-06.
 */
public final class CfgParser {
    public static String STD_PATH = "config.txt";

    private final static CfgParser parser = new CfgParser(STD_PATH);

    private BufferedReader reader;
    private String cfg = "";

    public CfgParser(String filePath){
        loadFile(filePath);
    }

    public static int readInt(String varName){

        return Integer.valueOf(parser.readValue(varName));
    }

    public long readLong(String varName) {
        return Long.valueOf(parser.readValue(varName));
    }

    public double readDouble(String varName){
        return Double.valueOf(parser.readValue(varName));
    }

    public String readString(String varName){
        return parser.readValue(varName);
    }

    public boolean readBoolean(String varName){
        return Boolean.valueOf(parser.readValue(varName));
    }

    public Class readClass(String varName) throws ClassNotFoundException{
        return Class.forName(parser.readValue(varName));
    }

    private String readValue(String varName){
        for(int i = 0; i < cfg.length(); i++){ //Read the whole string
            if(cfg.charAt(i) == '#'){ //Search for #
                if(cfg.regionMatches(false, i+1, varName, 0, varName.length())){ //See if name matches
                    String value = "";

                    //Current index + # + varName + "
                    int j = i + 1 + varName.length() + 2;
                    while(cfg.charAt(j) != '"'){
                        value += cfg.charAt(j);
                        j++;
                    }
                    return value;
                }
            }
        }

        System.out.println("Found no variable named " + varName + "!");
        return null;
    }

    private void loadFile(String filePath){
        try{
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream is = classLoader.getResourceAsStream(filePath);
            reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while((line = reader.readLine()) != null){
                cfg += line;
            }
        }catch(IOException e){
            System.out.println("Unable to load file: " + filePath);
        }
    }
}
