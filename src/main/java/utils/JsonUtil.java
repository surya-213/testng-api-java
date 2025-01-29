package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import exceptions.UtilException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtil {

  /**
   * Reads a JSON file and parses it as a JsonElement.
   *
   * @param filePath the path to the JSON file
   * @return the parsed JsonElement
   * @throws UtilException if the file is not found
   */
  public static JsonElement readFile(String filePath) {
    try {
      return JsonParser.parseReader(new FileReader(filePath));
    } catch (FileNotFoundException e) {
      throw new UtilException("File not found: %s".formatted(filePath), e);
    }
  }

}
