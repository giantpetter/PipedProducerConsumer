package socketChannel;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PipeTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        PipedOutputStream out = new PipedOutputStream();
        PipedInputStream in = new PipedInputStream();
        in.connect(out);
        Message msg = new Message(in, out);
        new Thread(() -> {
            for (int i = 0; i < 100; ++i) {
                msg.send(i);
            }
            msg.closeConnect();
        }, "sender").start();
        new Thread(() -> {
//            for (int i = 0; i < 100; ++i) {
            msg.receive();
//            }
        }, "receiver").start();
        while (Thread.activeCount() > 2) {
            TimeUnit.SECONDS.sleep(2);
        }
        msg.closeConnect();
    }
}

class Message {
    private PipedInputStream in;
    private PipedOutputStream out;

    public Message(PipedInputStream in, PipedOutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void closeConnect() {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(int i) {
        try {
            String text = "the " + i + "th msg \n";
            out.write(text.getBytes(), 0, text.length());
            System.out.println(Thread.currentThread().getName() + " send " + i + " ok");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receive() {
        byte[] bytes = new byte[1024 * 10];
        try {
            int len;
            while ((len = in.read(bytes)) > 0) {
                System.out.println("Receive from sender: " + new String(bytes, 0, len));
            }
            System.out.println(Thread.currentThread().getName() + " receive finished!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
