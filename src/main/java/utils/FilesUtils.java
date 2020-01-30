package utils;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class FilesUtils {

    /**
     * Récupère la liste des fichiers d'un dossier.
     *
     * @param folder     le dossier où chercher
     * @param files      la liste qui contiendra les résultats
     * @param extensions les extensions des fichiers à chercher
     */
    public static void listFilesForFolder(final File folder, List<File> files, List<String> extensions) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                files.add(fileEntry);
            } else if (extensions.stream()
                    .anyMatch(extension -> StringUtils.endsWith(fileEntry.getName(), extension))) {
                files.add(fileEntry);
            }
        }
    }
}
