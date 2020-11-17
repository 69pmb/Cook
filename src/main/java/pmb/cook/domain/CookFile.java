package pmb.cook.domain;

import java.util.Objects;

import pmb.cook.aspect.NoLogging;

@NoLogging
public class CookFile {

    private String name;
    private String fullName;
    private String extension;
    private String size;
    private String absolutePath;
    private Boolean isDirectory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public Boolean getIsDirectory() {
        return isDirectory;
    }

    public void setIsDirectory(Boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    @Override
    public int hashCode() {
        return Objects.hash(absolutePath, extension, fullName, isDirectory, name, size);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CookFile)) {
            return false;
        }
        CookFile other = (CookFile) obj;
        return Objects.equals(absolutePath, other.absolutePath) && Objects.equals(extension, other.extension)
                && Objects.equals(fullName, other.fullName) && Objects.equals(isDirectory, other.isDirectory) && Objects.equals(name, other.name)
                && Objects.equals(size, other.size);
    }

    @Override
    public String toString() {
        return "CookFile [fullName=" + fullName + "]";
    }

}
