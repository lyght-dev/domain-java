package domain;

import java.util.List;

public record Tags(List<Tag> values) {
    public Tags append(Tag tag) {
        return this;
    }

    public Tags remove(Tag tag) {
        return this;
    }

    public Tags clear() {
        return this;
    }
}
