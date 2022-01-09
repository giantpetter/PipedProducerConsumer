package socketChannel;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class TestSocketChannel {
    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress("localhost", 1234);
        final SocketChannel sc = SocketChannel.open();
        sc.connect(address);

        while(!sc.finishConnect()){
            System.out.println("waiting for connection ...");
        }
        System.out.println("connection success!");
        final ByteBuffer buffer = ByteBuffer.wrap("hello, this is client socket!" .getBytes() );
        while(buffer.hasRemaining()){
            sc.write(buffer);
        }

        InputStream input = sc.socket().getInputStream();

        final ReadableByteChannel channel = Channels.newChannel(input);
        buffer.clear();
        channel.read(buffer);
        buffer.flip();
        System.out.println(Charset.defaultCharset().decode(buffer));
        System.out.println("连接关闭");
        sc.close();


    }
}
