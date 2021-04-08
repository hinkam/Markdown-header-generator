import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Input file path:");
        String filePath = sc.next();
        sc.close();

        HeaderGenerator headerGenerator = new HeaderGenerator(filePath);
        headerGenerator.convert();
        headerGenerator.print();
        headerGenerator.changeFile();
    }

}
