package org.example.fileresolver;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class FileResolver {
    private static final Logger LOGGER = Logger.getLogger( FileResolver.class.getName() );

    public void getResource(String resource) throws NullPointerException, IOException {
        final URL fileUrl = getClass().getResource(resource);

        if (fileUrl != null) {
            String filePath = fileUrl.getPath();
            copyFilesToTmpDir(filePath);
        }
    }

    private Path getPathFromString(String stringPath) {
        Path path = null;

        if (stringPath.isBlank()) {
            return null;
        }
        try {
            path = Paths.get(stringPath);
        } catch (InvalidPathException e) {
            LOGGER.log(Level.SEVERE, "Failed to get Path from string.", e);
        }
        return path;
    }

    private String getTempPath() {
        String tempPath = "";
        try {
            tempPath = System.getProperty("java.io.tmpdir");
        } catch (NullPointerException | SecurityException e) {
            LOGGER.log(Level.SEVERE, "Failed to get tmpDir.", e);
            e.printStackTrace();
        }
        return tempPath;
    }

    private void createDirectory(Path path) throws IOException {
        Files.createDirectories(path);
        LOGGER.log(Level.INFO, "Created directory: " + path);
    }

    private void copyFilesToTmpDir(final String sourceUrl) throws IOException {
        final String tempPathTarget = getTempPath() + sourceUrl;
        System.out.println(tempPathTarget);

        Path targetPath = getPathFromString(tempPathTarget);
        Path sourcePath = getPathFromString(sourceUrl);

        if (sourcePath == null) {
            return;
        } else {
            createDirectory(targetPath);
        }

        try (Stream<Path> walk = Files.walk(sourcePath)) {
            walk.forEach(source -> {
                assert targetPath != null;
                Path destination = Paths.get(targetPath.toString(), source.toString()
                                .substring(sourceUrl.length()));
                        try {
                            LOGGER.log(Level.INFO, "Copying file: " + source);
                            Files.copy(source, destination);
                        } catch (IOException e) {
                            LOGGER.log(Level.SEVERE, "Failed to copy file:", e);
                        }
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
