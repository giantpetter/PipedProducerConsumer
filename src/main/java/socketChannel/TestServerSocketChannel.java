package socketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class TestServerSocketChannel {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 1234;
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);

        while (true) {
            System.out.println("我已经准备好了，等待接受连接！");
            SocketChannel sc = ssc.accept();
            if(sc != null){
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.put("hello, I'm from socketServer".getBytes());
                buffer.flip();
                sc.write(buffer);

                System.out.println("from socket client:" + sc.socket().getInetAddress());
                buffer.clear();
                sc.read(buffer);
                buffer.flip();
                System.out.println(Charset.defaultCharset().decode(buffer));
                sc.close();
            } else {
                TimeUnit.SECONDS.sleep(2);
            }

        }

    }
}
