import java.io.*;
import java.util.ArrayList;

public class Reader {
    public static ArrayList<String> readFile(String fileName) {

        //The files are read line by line and saved in the program.
        ArrayList<String> fileArray = new ArrayList<String>();
        String line;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                fileArray.add(line);
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
        return fileArray;
    }

    public static ArrayList<String> readFiles(String fileName) {
        ArrayList<String> fileArray = new ArrayList<String>();
        fileArray = readFile(fileName);
        return fileArray;
    }
}