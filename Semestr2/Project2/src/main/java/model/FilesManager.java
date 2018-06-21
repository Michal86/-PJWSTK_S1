package model;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Save/Load JSON
 */

public class FilesManager {

    public FilesManager(){}
    //==========================================

    public static String saveJson(Map<String, List<Player>> myMap) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        try {
            json = mapper.writeValueAsString(myMap);
            mapper.writeValue(getFile(), json);

            System.out.println(json);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }


    public static Hitlist getMapFromJSON(){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<Player>> hitlist;

        try {
            String text = new String(Files.readAllBytes(Paths.get("src/main/resources/saves/hitlist.json")), StandardCharsets.UTF_8);
            text = text.substring(1,text.length()-1).replaceAll("\\\\([\"/])", "$1");

            hitlist = mapper.readValue(text,
                    //new File("src/main/resources/saves/hitlist.json"),
                    new TypeReference<Map<String, List<Player>>>(){});

            return new Hitlist(hitlist);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Hitlist();
    }

    private static File getFile(){
        File newFile = null;
        try {
            if ((newFile = new File("src/main/resources/saves/hitlist.json")).exists()) {
                newFile.delete();
            }

            newFile = new File("src/main/resources/saves/hitlist.json");
            return newFile;
        }
        catch (Exception e){

            e.printStackTrace();
        }
        return newFile;
    }


}
