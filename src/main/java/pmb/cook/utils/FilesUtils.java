package pmb.cook.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import pmb.cook.aspect.Logging;
import pmb.cook.aspect.NoLogging;
import pmb.cook.domain.AbstractCook;
import pmb.cook.domain.Child;
import pmb.cook.domain.CookFile;
import pmb.cook.domain.GrandChild;
import pmb.cook.domain.Parent;

public final class FilesUtils {

    private static final List<String> EXTENSIONS = List.of("png", "jpg", "jpeg", "pdf");
    private static final String ROOT_DIRECTORY = System.getProperty("user.dir");
    private static final String DOCS = ROOT_DIRECTORY + File.separator + "docs";
    private static final String ASSETS_DIR = "." + File.separator + "assets" + File.separator;
    private static final String ARROW_LEFT = "left.svg";
    private static final String ARROW_UP = "up.svg";
    private static final String ARROW_RIGHT = "right.svg";
    public static final String MD_EXT = ".md";
    public static final String HTML_EXT = ".html";
    public static final String INDEX_FILE = "index" + MD_EXT;

    private FilesUtils() {}

    /**
     * Récupère la liste des fichiers d'un dossier.
     *
     * @param folder
     *            le dossier où chercher
     */
    @NoLogging
    private static List<CookFile> getCookFilesInFolder(final String folder) {
        Path pathFolder = Path.of(folder);
        try (Stream<CookFile> files = Files.list(pathFolder)
                .filter(fileEntry -> Files.isDirectory(fileEntry)
                        || EXTENSIONS.stream().anyMatch(extension -> StringUtils.endsWith(fileEntry.getFileName().toString(), extension)))
                .map(FilesUtils::pathToCookFile);) {
            return files.collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error listing files in: " + pathFolder.toFile().getAbsolutePath(), e);
        }
    }

    public static List<Parent> getParents() {
        Comparator<AbstractCook> comparator = Comparator.comparing(c -> c.getFile().getName());
        List<Parent> parents = getCookFilesInFolder(DOCS).stream().map(Parent::new).sorted(comparator).collect(Collectors.toList());
        parents.stream().filter(parent -> parent.getFile().getIsDirectory()).sorted(comparator).forEach(parent -> parent.setChildren(
                FilesUtils.getCookFilesInFolder(parent.getFile().getAbsolutePath()).stream().map(Child::new).collect(Collectors.toList())));
        parents.stream().map(Parent::getChildren).flatMap(List::stream).filter(child -> child.getFile().getIsDirectory()).sorted(comparator)
                .forEach(child -> child.setGrandChildren(FilesUtils.getCookFilesInFolder(child.getFile().getAbsolutePath()).stream()
                        .map(GrandChild::new).collect(Collectors.toList())));
        return parents;
    }

    @NoLogging
    private static CookFile pathToCookFile(Path path) {
        CookFile cookFile = new CookFile();
        cookFile.setFullName(path.getFileName().toString());
        boolean isDirectory = Files.isDirectory(path);
        cookFile.setIsDirectory(isDirectory);
        if (!isDirectory) {
            cookFile.setExtension(StringUtils.substringAfterLast(path.getFileName().toString(), "."));
            cookFile.setName(WordUtils.capitalizeFully(StringUtils.substringBeforeLast(path.getFileName().toString(), ".")));
        } else {
            cookFile.setName(cookFile.getFullName());
        }
        cookFile.setSize(FileUtils.byteCountToDisplaySize(path.toFile().length()));
        cookFile.setAbsolutePath(path.toAbsolutePath().toString());
        return cookFile;
    }

    @Logging(logArgs = false)
    public static void writeFileResult(StringBuilder sb, String file) throws IOException {
        Path resultFile = Paths.get(ROOT_DIRECTORY.concat(File.separator).concat(file));
        Files.deleteIfExists(resultFile);
        Files.write(resultFile, sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    }

    public static List<String> getArrows() {
        return List.of(ARROW_LEFT, ARROW_UP, ARROW_RIGHT).stream().map(arrow -> ASSETS_DIR + arrow).collect(Collectors.toList());
    }

}
