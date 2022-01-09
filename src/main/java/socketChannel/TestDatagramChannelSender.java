package socketChannel;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class TestDatagramChannelSender {
    public static void main(String[] args) throws IOException {
        final DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);

        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            buffer.clear();
            String msg = scanner.nextLine();
            buffer.put(msg.getBytes());
            buffer.flip();
            channel.send(buffer, new InetSocketAddress("localhost", 8899));

            buffer.clear();
            InetSocketAddress receive = (InetSocketAddress) channel.receive(buffer);
            while (receive == null) {
                receive = (InetSocketAddress) channel.receive(buffer);
            }
            buffer.flip();
//            final CharBuffer receiver = Charset.defaultCharset().decode(buffer);
            System.out.println("Get message from server:" + receive.getPort() + "----> " +
                   new String(buffer.array(), 0, buffer.limit()));

        }
    }
}
