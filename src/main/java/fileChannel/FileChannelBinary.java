package fileChannel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class FileChannelBinary {
    public static void main(String[] args) {
        //out.txt ---> out3.txt
        File file = new File("out.txt");
        try (
                FileChannel inChannel = new RandomAccessFile(file, "rw").getChannel();
                FileChannel outChannel = new FileOutputStream("out3.txt").getChannel();
        ) {
//            outChannel.transferFrom(inChannel, 0, file.length());
            inChannel.transferTo(0, file.length(), outChannel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
