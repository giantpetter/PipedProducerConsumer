package fileChannel;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * 内存映射文件--->把文件中的所有数据映射到虚拟内存中，这种访问文件效率较高
 */
public class FileChannelTest {
    public static void main(String[] args) {
        File file = new File("src/main/java/channel/FileChannelTest.java");
        try (
                FileChannel inChannel = new FileInputStream(file).getChannel();
                FileChannel outChannel = new FileOutputStream("out.txt").getChannel();
                ){
            //inchannel 文件中的内容映射到虚拟内存中
            MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            outChannel.write(buffer);

            buffer.flip();
            Charset charset = Charset.defaultCharset();
            CharBuffer decode = charset.decode(buffer);
            System.out.println(decode);
            System.out.println(buffer);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
