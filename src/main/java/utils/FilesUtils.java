package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class FilesUtils {

    /**
     * Récupère la liste des fichiers d'un dossier.
     *
     * @param folder     le dossier où chercher
     * @param files      la liste qui contiendra les résultats
     * @param extensions les extensions des fichiers à chercher
     * @param exclude    TODO
     */
    public static void listFilesForFolder(final Path folder, List<Path> files, List<String> extensions,
            String exclude) {
        List<Path> collect;
        try {
            collect = Files.list(folder).collect(Collectors.toList());
            for (final Path fileEntry : collect) {
                if (Files.isDirectory(fileEntry)) {
                    files.add(fileEntry);
                } else if (extensions.stream()
                        .anyMatch(extension -> StringUtils.endsWith(fileEntry.getFileName().toString(), extension))
                        && !StringUtils.equalsIgnoreCase(fileEntry.getFileName().toString(), exclude)) {
                    files.add(fileEntry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
