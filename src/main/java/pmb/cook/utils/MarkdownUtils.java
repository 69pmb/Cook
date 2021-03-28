package pmb.cook.utils;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import net.steppschuh.markdowngenerator.link.Link;
import pmb.cook.aspect.NoLogging;
import pmb.cook.domain.CookFile;

@NoLogging
public class MarkdownUtils {

    private StringBuilder sb;
    private final String NEW_LINE = "  \n";
    private final String FILE_HEADER = "---\r\ntitle: %s\r\n---";

    public MarkdownUtils() {
        sb = new StringBuilder();
    }

    public void buildLink(CookFile file, String parentFolder, String childFolder) {
        sb.append(new Link(file.getName(), MarkdownUtils.buildPath(parentFolder, childFolder, file.getFullName()))).append(" (")
                .append(file.getSize()).append(" ").append(file.getExtension()).append(")").append(NEW_LINE);
    }

    public void buildFileHeader(String title) {
        sb.append(String.format(FILE_HEADER, Optional.ofNullable(title).orElse("Recettes"))).append(NEW_LINE);
    }

    public void buildHeadind(String heading, Long count, int level) {
        sb.append(StringUtils.repeat("#", level) + " " + heading + Optional.ofNullable(count).map(c -> " (" + c + ")").orElse("")).append(NEW_LINE);
    }

    public void buildLinkPage(String parentName, long l) {
        buildHeadind(new Link(parentName, parentName + FilesUtils.MD_EXT).toString(), l, 2);
    }

    public void buildLinkPage(String parentName, String childName, int grandChildrenCount) {
        buildHeadind(
                "- " + new Link(childName, parentName + FilesUtils.MD_EXT + "#"
                        + StringUtils.replace(StringUtils.lowerCase(childName).toString(), " ", "-") + "-" + grandChildrenCount),
                Long.valueOf(grandChildrenCount), 3);
    }

    public static String buildPath(String parentFolder, String childFolder, String file) {
        Function<String, String> addSep = folder -> Optional.ofNullable(folder).map(dir -> folder + File.separator).orElse("");
        return "." + File.separator + "docs" + File.separator + addSep.apply(parentFolder) + addSep.apply(childFolder) + Path.of(file);
    }

    public void buildArrowsHeader(String left, String right) {
        String[] array = FilesUtils.getArrows().toArray(new String[0]);
        String leftArrow = Optional.ofNullable(left).map(path -> MarkdownUtils.buildImgLink(array[0], "Page précedente", path + FilesUtils.HTML_EXT))
                .orElse("");
        String rightArrow = Optional.ofNullable(right).map(path -> MarkdownUtils.buildImgLink(array[2], "Page suivante", path + FilesUtils.HTML_EXT))
                .orElse("");

        sb.append("<p align=\"justify\">").append(leftArrow).append(MarkdownUtils.buildImgLink(array[1], "Page parente", ".")).append(rightArrow)
                .append("</p>").append(NEW_LINE);
    }

    public static String buildImgLink(String img, String tooltip, String url) {
        return "<a href=\"" + url + "\"><img src=\"" + img + "\" title=\"" + tooltip + "\" style=\"height: 5vh\" /></a>";
    }

    public StringBuilder getSb() {
        return sb;
    }

}
