import java.io.*;
import java.util.*;

public class HeaderGenerator {
    private String filePath;
    private ArrayList<String> fileData = new ArrayList<>();
    private ArrayList<String> transformedHeaders = new ArrayList<>();

    HeaderGenerator(String filePath) {
        this.filePath = filePath;
    }

    // Unfortunately, i was unable to find a better solution for adding multiple tabulations at the beginning of a string
    private String createTabulation(int sharpStringLength) {
        String tabulation = "";
        if (sharpStringLength - 1 > 0)
            for (int i = 0; i < sharpStringLength - 1; i++)
                tabulation += "\t";
        return tabulation;
    }


    private String transformHeader(String[] splitArray) {
        String tabulation = createTabulation(splitArray[0].length());
        String headerID = splitArray[1].trim().toLowerCase().replace(' ', '-');
        return String.format("%s1. [%s](#%s)", tabulation, splitArray[1], headerID);
    }

    public void convert() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = br.readLine()) != null) {
                fileData.add(line);
                String[] splitArray = line.split(" ", 2);
                if (!(splitArray[0].matches("[^#]*"))) {
                    transformedHeaders.add(transformHeader(splitArray));
                }
            }
            transformedHeaders.add(" ");
            transformedHeaders.addAll(fileData);
        } catch (IOException e) {
            System.out.println("File Input Error");
        }
    }

    public void print() {
        if (!transformedHeaders.isEmpty()) {
            for (String transformedHeader : transformedHeaders) {
                System.out.println(transformedHeader);
            }
        }
    }

}
