package org.example;

import org.example.fileresolver.FileResolver;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {

    public static void main( String[] args ) throws IOException {
        FileResolver fileResolver = new FileResolver();
        final String folderPath = "/myfolder";

        fileResolver.getResource(folderPath);
    }
}
