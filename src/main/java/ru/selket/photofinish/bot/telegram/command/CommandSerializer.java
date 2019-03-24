package ru.selket.photofinish.bot.telegram.command;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.json.JSONException;
import org.json.JSONObject;

@Log
public class CommandSerializer {

    public static String serialize(Command command){
        try {
            String serialized = new ObjectMapper().writeValueAsString(command)
                    .replaceAll("\"([a-z]+?)\":", "$1:")
                    .replaceAll(":true", ":t")
                    .replaceAll(":false", ":f")
                    .replaceAll(":null", ":n");
            log.fine("serialized: " + serialized);
            return serialized;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Command deserialize(String input){
        input = input.replaceAll(":t", ":true")
                .replaceAll(":f", ":false")
                .replaceAll(":n", ":null");

        try {
            JSONObject jsonObject = new JSONObject(input);

            if (!jsonObject.has("c") || jsonObject.isNull("c")){
                throw new CommandSerializerException("No chat");
            }

            Command command = new Command();
            command.setCode(CommandCode.valueOf(jsonObject.getString("c")));

            if (jsonObject.has("p")){
                command.setParam(jsonObject.isNull("p") ? null : jsonObject.getString("p"));
            }

            return command;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
