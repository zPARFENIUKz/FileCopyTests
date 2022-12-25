import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private static final String src = "Files\\src.pdf"; // "Files\\srsSmall.txt" - small file to small file copying comparison
    private static final String dest = "Files\\dest.pdf";  // "Files\\dest.txt" - actually doesn't matter

    public static void main(String[] args) throws IOException {
        File srcFile = new File(src);
        File destFile = new File(dest);
        destFile.deleteOnExit();
        Path srcPath = Path.of(src);
        Path destPath = Path.of(dest);
        long startMillis = System.currentTimeMillis();
        fileCopyIO(srcFile, destFile);
        //fileCopyNIO(srcFile, destFile);
        //fileCopyNIO2(srcPath, destPath);
        long endMillis = System.currentTimeMillis();
        System.out.println("Executing time: " + (endMillis - startMillis));
    }


    private static void fileCopyNIO(File src, File dest) throws IOException {
        try (FileInputStream srcInputStream = new FileInputStream(src);
             FileOutputStream destOutputStream = new FileOutputStream(dest);
             FileChannel srcFC = srcInputStream.getChannel();
             FileChannel destFC = destOutputStream.getChannel())
        {
            long count = srcFC.size();
            while (count > 0) {
                long transferred = srcFC.transferTo(destFC.position(), count, destFC);
                srcFC.position(srcFC.position() + transferred);
                count -= transferred;
            }
        }
    }
    private static void fileCopyIO(File src, File dest) throws IOException {
        final int BUFFER_SIZE = 1024 * 64;
        try (InputStream is = new FileInputStream(src);
             OutputStream os = new FileOutputStream(dest))
        {
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
        }
    }
    private static void fileCopyNIO2(Path src, Path dest) throws IOException {
        Files.copy(src, dest);
    }

}
