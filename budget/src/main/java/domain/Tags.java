package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Tags(List<Tag> values) {
    public static Tags empty() {
        return new Tags(List.of());
    }

    public Tags {
        values = List.copyOf(values);
        if (values.size() > 5) {
            throw new IllegalArgumentException("태그는 5개를 초과할 수 없습니다.");
        }
    }

    public static Tags of(String... values) {
        return new Tags(Arrays.stream(values).map(Tag::new).toList());
    }

    public List<String> texts() {
        return values.stream().map(Tag::value).toList();
    }

    public Tags append(Tag tag) {
        var values = new ArrayList<>(this.values);
        values.add(tag);
        return new Tags(values);
    }

    public Tags remove(Tag tag) {
        var values = new ArrayList<>(this.values);
        values.remove(tag);
        return new Tags(values);
    }

    public Tags clear() {
        return new Tags(List.of());
    }
}
