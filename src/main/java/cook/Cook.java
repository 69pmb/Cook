package cook;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import cook.domain.Child;
import cook.domain.Parent;
import cook.utils.FilesUtils;
import cook.utils.MarkdownUtils;

public class Cook {

    public static void main(String[] args) throws Exception {
        List<Parent> parents = FilesUtils.getParents();
        buildIndexFile(parents);
        List<Parent> folderParent = parents.stream().filter(p -> p.getFile().getIsDirectory())
                .collect(Collectors.toList());
        for (int i = 0; i < folderParent.size(); i++) {
            String previous = i > 0 ? folderParent.get(i - 1).getFile().getFullName() : null;
            String next = i < folderParent.size() - 1 ? folderParent.get(i + 1).getFile().getFullName() : null;
            buildPage(folderParent.get(i), previous, next);
        }
    }

    public static void buildIndexFile(List<Parent> parents) throws IOException {
        MarkdownUtils mark = new MarkdownUtils();
        mark.buildFileHeader(null);
        mark.buildHeadind("Recettes:", null, 1);
        parents.stream().filter(Parent::isDirectory).forEach(parent -> {
            mark.buildLinkPage(parent.getName(), parent.countChildrenAndGrandChildren());
            parent.getChildren().stream().filter(Child::isDirectory).forEach(
                    child -> mark.buildLinkPage(parent.getName(), child.getName(), child.getGrandChildren().size()));
            List<Child> childrenNotDir = parent.getChildren().stream().filter(Predicate.not(Child::isDirectory))
                    .collect(Collectors.toList());
            if (childrenNotDir.size() != parent.getChildren().size()) {
                mark.buildLinkPage(parent.getName(), "Divers", childrenNotDir.size());
            }
        });

        List<Parent> parentsNotDir = parents.stream().filter(Predicate.not(Parent::isDirectory))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(parentsNotDir)) {
            mark.buildHeadind("Divers", Long.valueOf(parentsNotDir.size()), 2);
            parentsNotDir.forEach(parent -> mark.buildLink(parent.getFile(), null, null));
        }
        FilesUtils.writeFileResult(mark.getSb(), FilesUtils.INDEX_FILE);
    }

    private static void buildPage(Parent parent, String previous, String next) throws IOException {
        MarkdownUtils mark = new MarkdownUtils();
        mark.buildFileHeader(parent.getName());
        mark.buildHeadind(parent.getName(), null, 1);
        mark.buildArrowsHeader(previous, next);
        parent.getChildren().stream().filter(Child::isDirectory).forEach(child -> {
            mark.buildHeadind(child.getName(), Long.valueOf(child.getGrandChildren().size()), 1);
            child.getGrandChildren()
            .forEach(grandChild -> mark.buildLink(grandChild.getFile(), parent.getName(), child.getName()));
        });

        List<Child> childrenNotDir = parent.getChildren().stream().filter(Predicate.not(Child::isDirectory))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(childrenNotDir)) {
            if (childrenNotDir.size() != parent.getChildren().size()) {
                mark.buildHeadind("Divers", Long.valueOf(childrenNotDir.size()), 1);
            }
            childrenNotDir.forEach(child -> mark.buildLink(child.getFile(), parent.getName(), null));
        }
        FilesUtils.writeFileResult(mark.getSb(), parent.getName() + FilesUtils.MD_EXT);
    }
}
