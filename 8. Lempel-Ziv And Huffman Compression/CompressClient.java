package Ferdig.Komprimering;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CompressClient {
    static LempelZivKomprimering LZCompress = new LempelZivKomprimering();
    static LempelZivDekomprimering LZDeCompress = new LempelZivDekomprimering();
    static Compression HuffCompress = new Compression();
    static Decompression HuffDeCompress = new Decompression();

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please choose to:");
        System.out.println("1. Compress");
        System.out.println("2. Decompress");
        System.out.println("Anything else to exit");
            String choice = sc.next();
            switch (choice) {
                case "1":
                    System.out.println("Input filename: ");
                    String fileName = sc.next();
                    LZCompression(fileName);
                    HuffCompression();
                    break;
                case "2":
                    System.out.println("Output filename: ");
                    fileName = sc.next();
                    HuffDeCompression();
                    LZDeCompression(fileName);
                    break;
                default:
                    System.out.println("Exiting...");
                    break;
            }
    }

    public static void LZCompression(String fileName) throws IOException {
        LZCompress.initFiles(fileName, "Tempcompressed.txt");
        LZCompress.LempelZiv();
    }
    public static void LZDeCompression(String fileName) throws IOException {
        LZDeCompress.initFiles("decompressed.txt", fileName);
        LZDeCompress.Dekomprimering();
        File file = new File("decompressed.txt");
        file.deleteOnExit();

    }
    public static void HuffCompression() throws IOException {
        HuffCompress.compressFile("Tempcompressed.txt");
    }
    public static void HuffDeCompression() throws IOException {
        HuffDeCompress.decompressFile("compressed.txt");
    }
}
