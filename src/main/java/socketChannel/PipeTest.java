package socketChannel;

import sun.jvm.hotspot.runtime.Bytes;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.channels.Pipe;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PipeTest {
    public static void main(String[] args) throws IOException, InterruptedException {
//        Pipe pipe = Pipe.open();
        PipedOutputStream out = new PipedOutputStream();
        PipedInputStream in = new PipedInputStream();
        in.connect(out);
        Message msg = new Message(in, out);
        new Thread(() -> {
            for (int i = 0; i < 100; ++i) {
                msg.send(i);
            }
        }, "sender").start();
        new Thread(() -> {
            for(int i = 0; 1 < 100; ++i) {
                msg.receive();
            }
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
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private int num = 0;

    public Message(PipedInputStream in, PipedOutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void closeConnect() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(int i) {
        try {
            lock.lock();

            while (num != 0) {
                condition.await();
            }
//            out.connect(in);
            num = 1;
            String text = "hello! receiver: " + i + "\n";
            out.write(text.getBytes());
            out.flush();
            System.out.println(Thread.currentThread().getName() + " send " + i + " ok");
//            out.close();
//            in.close();
            condition.signal();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void receive() {
        byte[] bytes = new byte[1024 * 10];
        try {
            lock.lock();

            while (num != 1) {
                condition.await();
            }
//            in.connect(out);
            num = 0;
            int len = -1;
            while ((len = in.read(bytes)) > 0) {
                System.out.println(new String(bytes, 0, len));
            }
            System.out.println(Thread.currentThread().getName() + " receive finished!");
//            in.close();
//            out.close();
            condition.signal();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }
}
