package repository;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class CsvCodecTest {
    @Test
    void CSV_행을_원래_값으로_복원한다() {
        var values = List.of("메모, 내용", "\"인용\"", "태그|이름");

        assertThat(CsvCodec.readRow(CsvCodec.writeRow(values))).isEqualTo(values);
    }

    @Test
    void 빈_CSV_행은_빈_값_목록으로_복원한다() {
        assertThat(CsvCodec.readRow("")).isEmpty();
    }

    @Test
    void 중첩된_CSV_행에서_태그_구분자를_보존한다() {
        var tags = List.of("태그|이름", "경로\\이름");

        assertThat(CsvCodec.readRow(CsvCodec.writeRow(tags))).isEqualTo(tags);
    }
}
