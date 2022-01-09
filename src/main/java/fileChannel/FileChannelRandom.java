package fileChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * 可以同时读写的 FileChannel
 */
public class FileChannelRandom {
    public static void main(String[] args) {
        File file = new File("out.txt");
        try (
                RandomAccessFile rwFile = new RandomAccessFile(file, "rw");
                FileChannel channel = rwFile.getChannel();
        ) {
            MappedByteBuffer mapBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());
            channel.position(file.length());
            final Charset charset = Charset.defaultCharset();
            final CharsetDecoder charsetDecoder = charset.newDecoder();
            final CharBuffer charBuffer = charsetDecoder.decode(mapBuffer);
            final CharsetEncoder charsetEncoder = charset.newEncoder();
            final ByteBuffer encode = charsetEncoder.encode(charBuffer);
            channel.write(encode);
            System.out.println(mapBuffer.position());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
