package com.shanmh.imagestore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Hello world!
 */
public class App {

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp", "bmp", "ico");
    private static final String IMAGE_URL_PREFIX = "https://cdn.jsdelivr.net/gh/shanmh47/imgstore/";

    private static String imageDirPath = "";

    static {
        if (imageDirPath == null || imageDirPath.length() == 0) {
            try {
                Process p = Runtime.getRuntime().exec("pwd");
                byte[] bytes = readBytes(p.getInputStream());
                imageDirPath = new String(bytes).replace("\n", "");
            } catch (IOException ignore) {

            }
        }
    }

    private static byte[] readBytes(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[65536];
        int offset = 0;

        while (true) {
            int readCount = inputStream.read(bytes, offset, bytes.length - offset);
            if (readCount == -1) {
                return Arrays.copyOfRange(bytes, 0, offset);
            }

            offset += readCount;
            if (offset == bytes.length) {
                byte[] newBytes = new byte[bytes.length * 3 / 2];
                System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
                bytes = newBytes;
            }
        }
    }

    public static void main(String[] args) {
        File imageDir = new File(imageDirPath);
        File[] images = imageDir.listFiles(file -> {
            return !file.isDirectory() && IMAGE_EXTENSIONS.contains(getFileExtension(file));
        });
        if (images == null) {
            return;
        }
        for (File image : images) {
            process(image);
        }
    }

    private static String getFileExtension(File file) {
        int index = file.getName().lastIndexOf(".");
        if (index == -1) {
            return null;
        }
        return file.getName().substring(index + 1).toLowerCase();
    }

    private static void process(File image) {
        String imageStorePath = image.getParentFile().getParent();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String extension = getFileExtension(image);
        String newFileName = uuid + "." + extension;
        String newImagePath = imageStorePath + File.separator + newFileName;
        image.renameTo(new File(newImagePath));
        System.out.println(IMAGE_URL_PREFIX + newFileName);
    }
}
