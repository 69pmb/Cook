package cook;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import utils.FilesUtils;

public class Cook {

    private static final List<String> EXTENSIONS = List.of("png", "jpg", "jpeg", "pdf");
    public static final String DIRECTORY = System.getProperty("user.dir");
    public static final Function<Path, String> GET_FILE_NAME = file -> StringUtils
            .substringBeforeLast(file.getFileName().toString(), ".");
    public static final Comparator<Path> FILE_NAME_COMPARATOR = (c1, c2) -> String.CASE_INSENSITIVE_ORDER
            .compare(GET_FILE_NAME.apply(c1), GET_FILE_NAME.apply(c2));
    public static final String RESULT_FILE = "Cook.md";
    public static final String PREFIX_FILE = "file://";

    public static void main(String[] args) throws Exception {
        Path dir = Paths.get(DIRECTORY);
        List<Path> files = new ArrayList<>();
        FilesUtils.listFilesForFolder(dir, files, EXTENSIONS, RESULT_FILE);
        if (files.isEmpty()) {
            throw new Exception("Dossier: " + dir + " vide");
        }
        StringBuilder sb = new StringBuilder();
        files.stream().filter(Files::isDirectory).forEach(file -> {
            sb.append(new Heading(file.getFileName().toString(), 1)).append("\n");
            List<Path> children = new ArrayList<>();
            FilesUtils.listFilesForFolder(file, children, EXTENSIONS, RESULT_FILE);
            children.stream().sorted(FILE_NAME_COMPARATOR)
            .forEach(child -> sb
                    .append(new Link(GET_FILE_NAME.apply(child),
                            "." + File.separator + file.getFileName() + File.separator + child.getFileName()))
                    .append("\n"));
        });
        sb.append(new Heading("Divers", 1)).append("\n");
        files.stream().filter(Predicate.not(Files::isDirectory)).sorted(FILE_NAME_COMPARATOR).forEach(
                f -> sb.append(new Link(GET_FILE_NAME.apply(f), "." + File.separator + f.getFileName())).append("\n"));
        Path resultFile = Paths.get(DIRECTORY.concat(File.separator).concat(RESULT_FILE));
        Files.deleteIfExists(resultFile);
        Files.write(resultFile, sb.toString().getBytes(Charset.forName("UTF-8")), StandardOpenOption.CREATE);
    }
}
