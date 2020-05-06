package cook;

import java.util.List;

import cook.domain.Child;
import cook.domain.Parent;
import utils.FilesUtils;
import utils.MarkdownUtils;

public class Cook {

    public static void main(String[] args) throws Exception {
        List<Parent> parents = FilesUtils.getParents();
        if (parents.isEmpty()) {
            throw new Exception("Dossier vide");
        }
        MarkdownUtils mark = new MarkdownUtils();
        mark.buildFileHeader(null);

        parents.stream().filter(p -> p.getFile().getIsDirectory()).forEach(parent -> {
            mark.buildLinkPage(parent);
            parent.getChildren().stream().filter(Child::isDirectory)
            .forEach(child -> mark.buildLinkPage(parent, child));
        });

        //        mark.buildLinkPage((parents.stream().filter(Parent::isDirectory).count() + 1) + ". Divers",
        //                parents.stream().filter(Predicate.not(Parent::isDirectory)).count());
        //        parents.stream().filter(Predicate.not(Parent::isDirectory))
        //        .forEach(parent -> mark.buildLink(parent.getFile(), null));
        FilesUtils.writeFileResult(mark.getSb());
    }
}
