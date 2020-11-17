package pmb.cook.domain;

import pmb.cook.aspect.NoLogging;

@NoLogging
public class GrandChild
        extends AbstractCook {

    public GrandChild(CookFile file) {
        super(file);
    }

}
