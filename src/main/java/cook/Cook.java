package cook;

import java.util.List;

import cook.domain.Parent;
import utils.FilesUtils;
import utils.MarkdownUtils;

public class Cook {

    public static void main(String[] args) throws Exception {
        List<Parent> parents = FilesUtils.getParents();
        if (parents.isEmpty()) {
            throw new Exception("Dossier vide");
        }
        StringBuilder sb = MarkdownUtils.buildFileHeader();
        parents.stream().filter(p -> p.getFile().getIsDirectory()).forEach(parent -> {
            sb.append(MarkdownUtils.buildHeadind(parent.getFile().getName()));
            parent.getChildren().forEach(
                    child -> sb.append(MarkdownUtils.buildLink(child.getFile(), parent.getFile().getFullName())));
        });
        sb.append(MarkdownUtils
                .buildHeadind((parents.stream().filter(p -> p.getFile().getIsDirectory()).count() + 1) + ". Divers"));
        parents.stream().filter(p -> !p.getFile().getIsDirectory())
        .forEach(parent -> sb.append(MarkdownUtils.buildLink(parent.getFile(), null)));
        FilesUtils.writeFileResult(sb);
    }
}
