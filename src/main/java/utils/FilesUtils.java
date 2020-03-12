package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class FilesUtils {

    /**
     * Récupère la liste des fichiers d'un dossier.
     *
     * @param folder     le dossier où chercher
     * @param extensions les extensions des fichiers à chercher
     * @param exclude    file to exclude
     */
    public static List<Path> listFilesForFolder(final Path folder, List<String> extensions, String exclude) {
        try {
            return Files.list(folder).map(fileEntry -> {
                if (Files.isDirectory(fileEntry) || extensions.stream()
                        .anyMatch(extension -> StringUtils.endsWith(fileEntry.getFileName().toString(), extension))
                        && !StringUtils.equalsIgnoreCase(fileEntry.getFileName().toString(), exclude)) {
                    return fileEntry;
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error listing files in: " + folder.toFile().getAbsolutePath(), e);
        }
    }
}
