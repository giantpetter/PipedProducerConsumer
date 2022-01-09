package selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class SelectorClient {
    public static void main(String[] args) throws IOException {
        int port = 1234;
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(port));
        Scanner scanner = new Scanner(System.in);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while(!socketChannel.finishConnect()){
            System.out.println("正在连接......");
        }
        while(true){
            System.out.println("请输入要发送的内容：");
            String msg = scanner.nextLine();
            if("close".equals(msg)){
                break;
            }
            buffer.put(msg.getBytes());
            buffer.flip();
            while (buffer.hasRemaining()){
                socketChannel.write(buffer);
            }
            buffer.clear();
        }
        socketChannel.close();

    }
}
