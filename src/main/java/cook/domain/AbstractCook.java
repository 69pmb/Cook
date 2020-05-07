package cook.domain;

import java.util.Objects;

public abstract class AbstractCook {
    CookFile file;

    public AbstractCook(CookFile file) {
        this.file = file;
    }

    public CookFile getFile() {
        return file;
    }

    public void setFile(CookFile file) {
        this.file = file;
    }

    public boolean isDirectory() {
        return file.getIsDirectory();
    }

    public String getName() {
        return file.getName();
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AbstractCook)) {
            return false;
        }
        AbstractCook other = (AbstractCook) obj;
        return Objects.equals(file, other.file);
    }
}
