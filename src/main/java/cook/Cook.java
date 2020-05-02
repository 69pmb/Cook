package cook;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import utils.FilesUtils;

public class Cook {

    private static final List<String> EXTENSIONS = List.of("png", "jpg", "jpeg", "pdf");
    public static final String DIRECTORY = System.getProperty("user.dir");
    public static final String DOCS = File.separator + "docs";
    public static final Function<Path, String> GET_FILE_NAME = file -> StringUtils
            .substringBeforeLast(file.getFileName().toString(), ".");
    public static final Comparator<Path> FILE_NAME_COMPARATOR = (c1, c2) -> String.CASE_INSENSITIVE_ORDER
            .compare(GET_FILE_NAME.apply(c1), GET_FILE_NAME.apply(c2));
    public static final String RESULT_FILE = "index.md";
    public static final String PREFIX_FILE = "file://";
    public static final String NEW_LINE = "  \n";
    public static final String FILE_HEADER = "---\r\ntitle: Recette\r\n---";

    public static void main(String[] args) throws Exception {
        Path dir = Paths.get(DIRECTORY + DOCS);
        List<Path> files = FilesUtils.listFilesForFolder(dir, EXTENSIONS, RESULT_FILE);
        if (files.isEmpty()) {
            throw new Exception("Dossier: " + dir + " vide");
        }
        StringBuilder sb = new StringBuilder(FILE_HEADER + NEW_LINE);
        files.stream().filter(Files::isDirectory).forEach(file -> {
            sb.append(new Heading(file.getFileName().toString(), 1)).append(NEW_LINE);
            FilesUtils.listFilesForFolder(file, EXTENSIONS, RESULT_FILE).stream().sorted(FILE_NAME_COMPARATOR).forEach(
                    child -> sb.append(new Link(GET_FILE_NAME.apply(child), buildPath(file, child))).append(NEW_LINE));
        });
        sb.append(new Heading((files.stream().filter(Files::isDirectory).count() + 1) + ". Divers", 1))
        .append(NEW_LINE);
        files.stream().filter(Predicate.not(Files::isDirectory)).sorted(FILE_NAME_COMPARATOR).forEach(
                f -> sb.append(new Link(GET_FILE_NAME.apply(f), buildPath(null, f.getFileName()))).append(NEW_LINE));
        Path resultFile = Paths.get(DIRECTORY.concat(File.separator).concat(RESULT_FILE));
        Files.deleteIfExists(resultFile);
        Files.write(resultFile, sb.toString().getBytes(Charset.forName("UTF-8")), StandardOpenOption.CREATE);
    }

    public static String buildPath(Path folder, Path file) {
        return "." + File.separator + "docs" + File.separator
                + Optional.ofNullable(folder).map(dir -> folder.getFileName() + File.separator).orElse("")
                + file.getFileName();
    }
}
