package buffer;

import java.nio.CharBuffer;

public class Test02 {
    public static void main(String[] args) {
        CharBuffer buffer = CharBuffer.allocate(16);

        buffer.put("hello, 我是谁？你好世界");
        buffer.flip();
        System.out.println(buffer);
        char[] chars = new char[12];
//        CharBuffer remainingBuffer = buffer.get(chars);
        System.out.println(chars);
//        System.out.println(remainingBuffer.limit());
        buffer.flip();
        while (buffer.hasRemaining()) {
            int len = Math.min(buffer.remaining(), chars.length);
            buffer.get(chars, 0, len);
            System.out.print(new String(chars, 0, len));
        }
    }
}
