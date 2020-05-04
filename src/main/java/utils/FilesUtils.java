package utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import cook.domain.AbstractCook;
import cook.domain.Child;
import cook.domain.CookFile;
import cook.domain.GrandChild;
import cook.domain.Parent;

public class FilesUtils {
    private static final List<String> EXTENSIONS = List.of("png", "jpg", "jpeg", "pdf");
    private static final String ROOT_DIRECTORY = System.getProperty("user.dir");
    private static final String DOCS = ROOT_DIRECTORY + File.separator + "docs";
    private static final String RESULT_FILE = "index.md";

    /**
     * Récupère la liste des fichiers d'un dossier.
     *
     * @param folder le dossier où chercher
     */
    private static List<CookFile> getCookFilesInFolder(final String folder) {
        Path pathFolder = Path.of(folder);
        try {
            return Files.list(pathFolder)
                    .filter(fileEntry -> Files.isDirectory(fileEntry) || EXTENSIONS.stream()
                            .anyMatch(extension -> StringUtils.endsWith(fileEntry.getFileName().toString(), extension))
                            && !StringUtils.equalsIgnoreCase(fileEntry.getFileName().toString(), RESULT_FILE))
                    .map(FilesUtils::pathToCookFile).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error listing files in: " + pathFolder.toFile().getAbsolutePath(), e);
        }
    }

    public static List<Parent> getParents() {
        Comparator<AbstractCook> comparator = Comparator.comparing(c -> c.getFile().getName());
        List<Parent> parents = getCookFilesInFolder(DOCS).stream().map(Parent::new).sorted(comparator)
                .collect(Collectors.toList());
        parents.stream().filter(parent -> parent.getFile().getIsDirectory()).sorted(comparator).forEach(
                parent -> parent.setChildren(FilesUtils.getCookFilesInFolder(parent.getFile().getAbsolutePath())
                        .stream().map(Child::new).collect(Collectors.toList())));
        parents.stream().map(Parent::getChildren).flatMap(List::stream)
        .filter(child -> child.getFile().getIsDirectory()).sorted(comparator)
        .forEach(child -> child
                .setGrandChildren(FilesUtils.getCookFilesInFolder(child.getFile().getAbsolutePath()).stream()
                        .map(GrandChild::new).collect(Collectors.toList())));
        return parents;
    }

    private static CookFile pathToCookFile(Path path) {
        CookFile cookFile = new CookFile();
        cookFile.setFullName(path.getFileName().toString());
        boolean isDirectory = Files.isDirectory(path);
        cookFile.setIsDirectory(isDirectory);
        if (!isDirectory) {
            cookFile.setExtension(StringUtils.substringAfterLast(path.getFileName().toString(), "."));
            cookFile.setName(
                    WordUtils.capitalizeFully(StringUtils.substringBeforeLast(path.getFileName().toString(), ".")));
        } else {
            cookFile.setName(cookFile.getFullName());
        }
        cookFile.setSize(FileUtils.byteCountToDisplaySize(path.toFile().length()));
        cookFile.setAbsolutePath(path.toAbsolutePath().toString());
        return cookFile;
    }

    public static void writeFileResult(StringBuilder sb) throws IOException {
        Path resultFile = Paths.get(ROOT_DIRECTORY.concat(File.separator).concat(RESULT_FILE));
        Files.deleteIfExists(resultFile);
        Files.write(resultFile, sb.toString().getBytes(Charset.forName("UTF-8")), StandardOpenOption.CREATE);
    }
}
