package cook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.steppschuh.markdowngenerator.text.heading.Heading;
import utils.FilesUtils;

public class Cook {

    private static final List<String> EXTENSIONS = List.of("png", "jpg", "jpeg", "pdf");

    /**
     * @param args folder containing cooking recipe
     */
    public static void main(String[] args) {
        if (args != null && args.length > 0) {
            File root = new File(args[0]);
            if (root.exists()) {
                List<File> files = new ArrayList<>();
                FilesUtils.listFilesForFolder(root, files, EXTENSIONS);
                StringBuilder sb = new StringBuilder();
                files.forEach(file -> {
                    sb.append(new Heading(file.getName(), 1)).append("\n");
                    if (file.isDirectory()) {
                        List<File> children = new ArrayList<>();
                        FilesUtils.listFilesForFolder(file, children, EXTENSIONS);
                        children.forEach(child -> sb.append(new Heading(child.getName(), 4)).append("\n"));
                    }
                });
                System.out.println(sb.toString());
            }
        }
    }

}
