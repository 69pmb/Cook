package utils;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import cook.domain.Child;
import cook.domain.CookFile;
import cook.domain.Parent;
import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.text.heading.Heading;

public class MarkdownUtils {

    private StringBuilder sb;
    private final String NEW_LINE = "  \n";
    private final String FILE_HEADER = "---\r\ntitle: %s\r\n---";

    public MarkdownUtils() {
        sb = new StringBuilder();
    }

    public void buildLink(CookFile file, String folder) {
        sb.append(new Link(file.getName(), MarkdownUtils.buildPath(folder, file.getFullName()))).append(" (")
        .append(file.getSize()).append(" ").append(file.getExtension()).append(")").append(NEW_LINE);
    }

    public void buildFileHeader(String title) {
        sb.append(String.format(FILE_HEADER, Optional.ofNullable(title).orElse("Recette"))).append(NEW_LINE);
    }

    public void buildHeadind(String heading, long count, int level) {
        sb.append(new Heading(heading + " (" + count + ")", level)).append(NEW_LINE);
    }

    public void buildLinkPage(Parent parent) {
        buildHeadind(new Link(parent.getFile().getName(), parent.getFile().getName() + ".md").toString(),
                parent.countChildrenAndGrandChildren(), 1);
    }

    public void buildLinkPage(Parent parent, Child child) {
        buildHeadind(
                new Link(child.getFile().getName(), parent.getFile().getName() + ".md#" + child.getFile().getName())
                .toString(),
                child.getGrandChildren().size(), 2);
    }

    public static String buildPath(String folder, String file) {
        return "." + File.separator + "docs" + File.separator
                + Optional.ofNullable(folder).map(dir -> folder + File.separator).orElse("") + Path.of(file);
    }

    public void buildArrowsHeader(String left, String right) {
        String[] array = FilesUtils.getArrows().toArray(new String[0]);
        String leftArrow = Optional.ofNullable(left)
                .map(path -> MarkdownUtils.buildImgLink(array[0], "Page précedente", path + FilesUtils.MD_EXT))
                .orElse("");
        String rightArrow = Optional.ofNullable(right)
                .map(path -> MarkdownUtils.buildImgLink(array[2], "Page suivante", path + FilesUtils.MD_EXT))
                .orElse("");

        sb.append("<p align=\"justify\">").append(leftArrow)
        .append(MarkdownUtils.buildImgLink(array[1], "Page parente", "..")).append(rightArrow).append("</p>")
        .append(NEW_LINE);
    }

    public static String buildImgLink(String img, String tooltip, String url) {
        return "<a href=\"" + url + "\"><img src=\"" + img + "\" title=\"" + tooltip
                + "\" style=\"height: 5vh\" /></a>";
    }

    public StringBuilder getSb() {
        return sb;
    }
}
