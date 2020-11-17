package pmb.cook.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pmb.cook.aspect.NoLogging;

@NoLogging
public class Child
        extends AbstractCook {

    private List<GrandChild> grandChildren;

    public Child(CookFile file) {
        super(file);
    }

    public List<GrandChild> getGrandChildren() {
        return grandChildren == null ? new ArrayList<>() : grandChildren;
    }

    public void setGrandChildren(List<GrandChild> grandChildren) {
        this.grandChildren = grandChildren;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = (prime * result) + Objects.hash(grandChildren);
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
        if (!(obj instanceof Child)) {
            return false;
        }
        Child other = (Child) obj;
        return Objects.equals(grandChildren, other.grandChildren);
    }

}
