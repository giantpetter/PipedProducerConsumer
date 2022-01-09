package socketChannel;

import java.io.IOException;
import java.nio.channels.Pipe;

public class PipeTest2 {
    public static void main(String[] args) {
        try {
            Pipe pipe = Pipe.open();
            final Pipe.SinkChannel sink = pipe.sink();
//            sink.wr
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class Message1 {

}
