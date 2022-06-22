package com.shanmh.imagestore;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class App {

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp", "bmp", "ico", "mp4");
    private static final String IMAGE_URL_PREFIX = "https://cdn.jsdelivr.net/gh/shanmh47/imgstore/";

    public static void main(String[] args) {
        File imageDir = new File(args[0]);
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
