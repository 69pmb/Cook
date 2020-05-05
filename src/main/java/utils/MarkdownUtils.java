package utils;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import cook.domain.CookFile;
import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.text.heading.Heading;

public class MarkdownUtils {

    private static final String NEW_LINE = "  \n";
    private static final String FILE_HEADER = "---\r\ntitle: Recette\r\n---";

    public static StringBuilder buildLink(CookFile file, String folder) {
        return new StringBuilder().append(new Link(file.getName(), MarkdownUtils.buildPath(folder, file.getFullName())))
                .append(" (").append(file.getSize()).append(" ").append(file.getExtension()).append(")")
                .append(NEW_LINE);
    }

    public static StringBuilder buildFileHeader() {
        return new StringBuilder(FILE_HEADER + NEW_LINE);
    }

    public static StringBuilder buildHeadind(String heading) {
        return new StringBuilder().append(new Heading(heading, 1)).append(NEW_LINE);
    }

    public static String buildPath(String folder, String file) {
        return "." + File.separator + "docs" + File.separator
                + Optional.ofNullable(folder).map(dir -> folder + File.separator).orElse("") + Path.of(file);
    }

    public static String buildArrowsHeader(String left, String right) {
        String[] array = FilesUtils.getArrows().toArray(new String[0]);
        return "<p align=\"justify\">" + MarkdownUtils.buildImgLink(array[0], "Page précedente", left)
        + MarkdownUtils.buildImgLink(array[1], "Page parente", "..")
        + MarkdownUtils.buildImgLink(array[2], "Page suivante", right) + "</p>" + NEW_LINE;
    }

    public static String buildImgLink(String img, String tooltip, String url) {
        return "<a href=\"" + url + "\"><img src=\"" + img + "\" title=\"" + tooltip
                + "\" style=\"height: 5vh\" /></a>";
    }
}
