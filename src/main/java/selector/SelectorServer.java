package selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class SelectorServer {
    static ServerSocketChannel ssc;
    static Selector selector;
    static ByteBuffer buffer;
    static int port = 1234;

    public static void main(String[] args) throws IOException {
        ssc = ServerSocketChannel.open();
        selector = Selector.open();
        ssc.configureBlocking(false);
        ssc.socket().bind(new InetSocketAddress(port));
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel socket = channel.accept();
                    socket.configureBlocking(false);
                    socket.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端已连接....");
                }
                if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    readDataFromSocketChannel(socketChannel);
                }
                iterator.remove();
            }
        }

    }

    private static void readDataFromSocketChannel(SocketChannel channel) throws IOException {
        StringBuilder data = new StringBuilder();
        buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        int n;
        while ((n = channel.read(buffer)) > 0) {
            buffer.flip();
//            char[] dst = new char[buffer.limit()];
//            for (int i = 0; i < dst.length; ++i) {
//                dst[i] = (char) buffer.get(i);
//            }
            String dst = new String(buffer.array(), 0, buffer.limit());
            data.append(dst);
            buffer.clear();
            n = channel.read(buffer);
        }

        if(n < 0){
            System.out.println("客户端已关闭...");
            channel.close();
        } else {
            System.out.println("服务器收到客户端的数据：" + data);
        }
    }
}
