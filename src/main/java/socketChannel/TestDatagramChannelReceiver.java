package socketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TestDatagramChannelReceiver {
    public static void main(String[] args) throws IOException, InterruptedException {
        final DatagramChannel channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(8899));
        channel.configureBlocking(false);

        Scanner scanner = new Scanner(System.in);
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            buffer.clear();
            InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);
            if (clientAddress == null) {
                TimeUnit.SECONDS.sleep(2);
            } else {
                System.out.print("Get message from: " + clientAddress.getAddress() + " :" + clientAddress.getPort());
                buffer.flip();
                String msg = Charset.defaultCharset().decode(buffer).toString();
                System.out.println("----> " + msg);

                buffer.clear();
                String sendMsg = scanner.nextLine();
//                buffer.put(sendMsg.getBytes());
                buffer.put("message received!".getBytes());
                buffer.flip();
                channel.send(buffer, clientAddress);
            }
        }
    }
}
