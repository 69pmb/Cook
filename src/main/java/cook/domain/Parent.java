package cook.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Parent extends AbstractCook {
    private List<Child> children;

    public Parent(CookFile file) {
        super(file);
    }

    public List<Child> getChildren() {
        return children == null ? new ArrayList<>() : children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(children);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Parent)) {
            return false;
        }
        Parent other = (Parent) obj;
        return Objects.equals(children, other.children);
    }
}
