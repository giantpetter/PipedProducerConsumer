package buffer;

import java.nio.CharBuffer;

public class Test01 {
    public static void main(String[] args) {
        CharBuffer buffer = CharBuffer.allocate(12);
        System.out.println("Capacity:" + buffer.capacity() + ",limit:"+ buffer.limit() + ",position:" + buffer.position());
        buffer.put('我').put('有').put('矿').put('你').put('爱').put('我').put('吗');

        System.out.println("Capacity:" + buffer.capacity() + ",limit:"+ buffer.limit() + ",position:" + buffer.position());
        buffer.flip();
        System.out.println("Capacity:" + buffer.capacity() + ",limit:"+ buffer.limit() + ",position:" + buffer.position());
        System.out.println(buffer.get());
        System.out.println("Capacity:" + buffer.capacity() + ",limit:"+ buffer.limit() + ",position:" + buffer.position());
        buffer.put('X');
        System.out.println("Capacity:" + buffer.capacity() + ",limit:"+ buffer.limit() + ",position:" + buffer.position());

        buffer.mark();
        System.out.println(buffer.get(1));
        buffer.reset();

        buffer.compact();
        buffer.clear();
        System.out.println(buffer);
        while(buffer.hasArray()){
            System.out.print(buffer.get());
        }
    }
}
