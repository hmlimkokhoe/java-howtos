package org.example;

import org.example.fileresolver.FileResolver;

public class App {

    public static void main( String[] args ) {
        FileResolver fileResolver = new FileResolver();
        final String folderPath = "/myfolder";

        fileResolver.copyResourcesToTmpDir(folderPath);
    }
}
