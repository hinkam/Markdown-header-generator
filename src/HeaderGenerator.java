import java.io.*;
import java.util.*;

public class HeaderGenerator {
    private String filePath;
    private ArrayList<String> fileData = new ArrayList<>();
    private ArrayList<String> transformedHeaders = new ArrayList<>();
    private Stack<Integer> hierarchyStack = new Stack<>();
    private HashMap<String, Integer> headerEntryCount = new HashMap<>();

    HeaderGenerator(String filePath) {
        this.filePath = filePath;
    }

    // Its easy to find out how much headers there are on current level by using Stack.
    private int refreshHierarchy(String sharps) {
        if (hierarchyStack.empty()) {
            hierarchyStack.push(1);
        } else if (hierarchyStack.size() == sharps.length()) {
            hierarchyStack.set(hierarchyStack.size() - 1, hierarchyStack.peek() + 1);
        } else if (hierarchyStack.size() < sharps.length()) {
            hierarchyStack.push(1);
        } else {
            while (hierarchyStack.size() > sharps.length()) {
                hierarchyStack.pop();
            }
            hierarchyStack.set(hierarchyStack.size() - 1, hierarchyStack.peek() + 1);
        }
        return hierarchyStack.peek();
    }

    // We can use HashMap to find out if there are any repetitive headers. This will allow us to create different anchors for them
    private int getHeaderEntryNumber(String currentHeader) {
        String refString = currentHeader.trim().toLowerCase();
        if (headerEntryCount.containsKey(refString)) {
            headerEntryCount.put(refString, headerEntryCount.get(refString) + 1);
        } else {
            headerEntryCount.put(refString, 1);
        }
        return headerEntryCount.get(refString);
    }

    // Unfortunately, i was unable to find a better solution for adding multiple tabulations at the beginning of a string
    private String createTabulation(int sharpStringLength) {
        StringBuilder tabulation = new StringBuilder();
        if (sharpStringLength - 1 > 0) {
            tabulation.append("\t".repeat(sharpStringLength - 1));
        }
        return tabulation.toString();
    }

    // Main method responsible for transforming the basic header string
    private String transformHeader(String[] splitArray) {
        int currentNumber = refreshHierarchy(splitArray[0]);
        int headerEntryNumber = getHeaderEntryNumber(splitArray[1]);
        String tabulation = createTabulation(splitArray[0].length());

        String headerID = splitArray[1].trim().toLowerCase().replace(' ', '-');
        if (headerEntryNumber > 1) {
            headerID += String.format("-%d", headerEntryNumber - 1);
        }

        return String.format("%s%d. [%s](#%s)", tabulation, currentNumber, splitArray[1], headerID);
    }

    public void convert() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                fileData.add(line);
                String[] splitArray = line.split(" ", 2);
                if (splitArray[0].matches("#*")) {
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
