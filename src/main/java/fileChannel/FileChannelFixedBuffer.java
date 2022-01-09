package fileChannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 固定缓存大小的读写 fixedBuffer
 */
public class FileChannelFixedBuffer {
    public static void main(String[] args) {
        try (

                FileChannel channel1 = new FileInputStream("out.txt").getChannel();
                FileChannel channel2 = new FileOutputStream("out2.txt").getChannel();
        ) {
            final ByteBuffer buffer = ByteBuffer.allocate(48);
            int result;
            do {
                result = channel1.read(buffer);
                buffer.flip();
                channel2.write(buffer);
                buffer.clear();
            } while (result != -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
