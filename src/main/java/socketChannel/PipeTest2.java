package socketChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PipeTest2 {
    public static void main(String[] args) throws IOException {
        Message2 msg = new Message2();
        new Thread(() -> {
            for (int i = 0; i < 100; ++i) {
                try {
                    msg.send(i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "sender").start();
        new Thread(() -> {
            for (int i = 0; i < 100; ++i) {
                try {
                    msg.receive();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "receiver").start();
    }
}

class Message2 {
    private Pipe pipe;
    private Pipe.SinkChannel sinkChannel;
    private Pipe.SourceChannel sourceChannel;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private int num = 0;

    public Message2() throws IOException {
        pipe = Pipe.open();
        sinkChannel = pipe.sink();
        sourceChannel = pipe.source();
    }

    public void send(int i) throws IOException {
        lock.lock();
        try {
            while (num != 0) {
                condition.await();
            }
            if(!sinkChannel.isOpen()){
                pipe = Pipe.open();
                sinkChannel = pipe.sink();
                sourceChannel = pipe.source();
            }
            num = 1;
            String msg = "消息：" + i;
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(msg.getBytes());
            buffer.flip();
            while (buffer.hasRemaining()) {
                sinkChannel.write(buffer);
            }
            System.out.println("第" + i + "条消息发送成功");

            sinkChannel.close();
            condition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void receive() throws IOException {
        lock.lock();
        try {
            while (num != 1) {
                condition.await();
            }
            num = 0;
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.clear();
            int bytesRead;
            while ((bytesRead = sourceChannel.read(buffer)) > 0) {

            }
            buffer.flip();
            System.out.println("收到" + new String(buffer.array(), 0, buffer.limit()));

            sourceChannel.close();
            condition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }
}
