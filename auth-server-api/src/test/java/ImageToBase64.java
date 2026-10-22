import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageToBase64 {

    public static String encodeImageToBase64(File imageFile) {
        String encodedString = "";
        try (FileInputStream imageInFile = new FileInputStream(imageFile)) {
            // Reading a Image file from file system
            byte[] imageData = new byte[(int) imageFile.length()];
            imageInFile.read(imageData);

            // Encoding string into Base64
            encodedString = Base64.getEncoder().encodeToString(imageData);
        } catch (IOException e) {
            System.out.println("Exception while reading the Image file: " + e.getMessage());
        }
        return encodedString;
    }

    public static void main(String[] args) {
        File imageFile = new File("/6052564391549755222.jpg"); // 替换为你的图片路径
        String base64Image = encodeImageToBase64(imageFile);
        System.out.println("Base64 Encoded Image: " + base64Image);
    }
}