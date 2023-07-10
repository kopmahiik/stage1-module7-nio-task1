package com.epam.mjc.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileReader {

    public Profile getDataFromFile(File file) {

        try (RandomAccessFile aFile = new RandomAccessFile(file, "r");
             FileChannel inChannel = aFile.getChannel()) {

            long fileSize = inChannel.size();

            ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);

            String linesStr = "";
            while (inChannel.read(buffer) > 0) {
                buffer.flip();
                for (int i = 0; i < buffer.limit(); i++) {
                    linesStr += (char) buffer.get();
                }
                buffer.clear();
            }

            String[] linesArr = linesStr.split("\n");

            List<String> list = Arrays.asList(linesArr);
            Map<String, String> map = parseToHashMap(list);
            return mapToProfile(map);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Profile();
    }

    public static Map<String, String> parseToHashMap(List<String> list) {
        HashMap<String, String> map = new HashMap<>();
        for (String str :
                list) {
            String[] splitedLine = str.split(":");
            map.put(splitedLine[0].trim(), splitedLine[1].trim());
        }
        return map;
    }

    public static Profile mapToProfile(Map<String, String> map) {
        String name = map.get("Name");
        Integer age = Integer.parseInt(map.get("Age"));
        String email = map.get("Email");
        Long phone = Long.parseLong(map.get("Phone"));

        return new Profile(name, age, email, phone);
    }


}
