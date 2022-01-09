package fileChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileChannelGather {
    public static void main(String[] args) {
        File file = new File("out1.txt");
        long lastModified = file.lastModified();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = date.format(new Date(lastModified));
        String fileName = file.getName();
        String headerContent = "fileName:" + fileName + "\n"
                + "path: " + file.getAbsolutePath() + "\n" + "lastModified:" + formatDate + "\n";
        ByteBuffer header = ByteBuffer.wrap(headerContent.getBytes());

        ByteBuffer contents;
        FileChannel inChannel;
        long contentLength = -1;
        String contentType = "unknown";
        try {
            inChannel = new FileInputStream(file).getChannel();
            contents = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            contentLength = inChannel.size();
            contentType = URLConnection.guessContentTypeFromName(fileName);
        } catch (Exception e) {
            contents = ByteBuffer.allocate(1024);
            String errMsg = "文件打开失败：" + e + "\n";
            contents.put(errMsg.getBytes());
            contents.flip();
        }

        ByteBuffer type = ByteBuffer.allocate(128);
        StringBuilder typeStr = new StringBuilder();
        typeStr.append("Content-Length:").append(contentLength).append("\n").
                append("Content-type:").append(contentType).append("\n");
        type.put(typeStr.toString().getBytes());
        type.flip();

        ByteBuffer[] gathering = {header, type, contents};
        try (FileChannel outChannel = new FileOutputStream("gathering.txt").getChannel()) {
            while(outChannel.write(gathering) > 0){
            }
        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
