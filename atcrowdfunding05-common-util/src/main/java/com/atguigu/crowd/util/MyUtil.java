package com.atguigu.crowd.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.stream.*;

public class MyUtil {

    public static void readFileByLine(int bufSize, FileChannel inChannel, ByteBuffer rBuffer,
                                      FileChannel outChannel, ByteBuffer wBuffer) {
        String enterStr = "\r\n";
        try {
            byte[] bs = new byte[bufSize];
            int size = 0;
            StringBuffer strBuf = new StringBuffer();
            while (inChannel.read(rBuffer) != -1) {
                int rSize = rBuffer.position();
                rBuffer.rewind();
                rBuffer.get(bs);
                rBuffer.clear();
                String tempString = new String(bs, 0, rSize);
                int fromIndex = 0;
                int endIndex = 0;
                while ((endIndex = tempString.indexOf(enterStr, fromIndex)) != -1) {
                    String line = tempString.substring(fromIndex, endIndex);
                    line = new String(strBuf.toString() + line);
                    writeFileByLine(outChannel, wBuffer, line);
                    strBuf.delete(0, strBuf.length());
                    fromIndex = endIndex + 1;
                }
                if (rSize > tempString.length()) {
                    strBuf.append(tempString.substring(fromIndex, tempString.length()));
                } else {
                    strBuf.append(tempString.substring(fromIndex, rSize));
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param outChannel
     * @param wBuffer
     * @param line
     */
    public static void writeFileByLine(FileChannel outChannel, ByteBuffer wBuffer,
                                       String line) {
        try {
            outChannel.write(wBuffer.wrap(line.getBytes()), outChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取姓名
     */
    public static ArrayList<String> readName(String fileName) {

        String name;
        ArrayList<String> names = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            while ((name = br.readLine()) != null) {
                System.out.println(name);
                names.add(name);
            }
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return names;
    }
}
