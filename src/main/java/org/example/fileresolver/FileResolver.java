package org.example.fileresolver;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class FileResolver {
    private static final Logger LOGGER = Logger.getLogger( FileResolver.class.getName() );

    public void getResource(String resource) throws NullPointerException {
        final URL fileUrl = getClass().getResource(resource);

        if (fileUrl != null) {
            String filePath = fileUrl.getPath();
            copyFilesToTmpDir(resource, filePath);
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

    private void emptyDirectory(Path path) {
        final File file = path.toFile();

        try {
            FileUtils.cleanDirectory(file);
            LOGGER.log(Level.INFO, "Existing directory found. Cleaning it to enable file copying process.");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"Existing directory files could not be deleted.", e);
        }

        /*try {
            FileUtils.deleteDirectory(file);
            LOGGER.log(Level.INFO, "Existing directory removed.");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"Directory could not be deleted.", e);
        }*/
    }

    private void createDirectory(Path path) {
        if (path.toFile().isDirectory()) {
            emptyDirectory(path);
        }

        try {
            Files.createDirectories(path);
            LOGGER.log(Level.INFO, "Created directory: " + path);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"Directory could not be created.", e);
        }
    }

    private void copyFilesToTmpDir(final String folderName, String sourceUrl) {
        final String tempPathTarget = getTempPath() + folderName;

        final Path targetPath = getPathFromString(tempPathTarget);
        final Path sourcePath = getPathFromString(sourceUrl);

        if (sourcePath == null || targetPath == null) {
            return;
        } else {
            createDirectory(targetPath);
        }

        try (Stream<Path> walk = Files.walk(sourcePath)) {
            walk.forEach(source -> {
                final String filePath = source.toString().substring(sourceUrl.length());
                Path destination = Paths.get(tempPathTarget, filePath);
                LOGGER.log(Level.INFO, "Copying file: " + destination);

                try {
                    LOGGER.log(Level.INFO, "Copying file: " + source + " TO: " + destination);
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Failed to copy file:", e);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
