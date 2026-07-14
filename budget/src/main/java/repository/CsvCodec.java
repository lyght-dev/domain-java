package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class CsvCodec {
    private CsvCodec() {
    }

    public static String writeRow(List<String> values) {
        return values.stream()
                .map(value -> "\"" + value.replace("\"", "\"\"") + "\"")
                .collect(Collectors.joining(","));
    }

    public static List<String> readRow(String row) {
        if (row.isEmpty()) {
            return List.of();
        }

        var values = new ArrayList<String>();
        var value = new StringBuilder();
        var quoted = false;

        for (var index = 0; index < row.length(); index++) {
            var character = row.charAt(index);
            if (character == '"') {
                if (quoted && index + 1 < row.length() && row.charAt(index + 1) == '"') {
                    value.append(character);
                    index++;
                } else {
                    quoted = !quoted;
                }
            } else if (character == ',' && !quoted) {
                values.add(value.toString());
                value.setLength(0);
            } else {
                value.append(character);
            }
        }
        values.add(value.toString());
        return values;
    }

}
