import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;

public class TestJsonViaParsing {

    @Test
    @DisplayName("test with JSON Object")
    public void jsonParserTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Ps4 ps4 = objectMapper.readValue(new File("src/test/resources/ps4.json"), Ps4.class);
        assertThat(ps4.name).containsIgnoringCase("PS4");
        assertThat(ps4.color).isEqualTo("black");
        assertThat(ps4.price).isBetween(29000, 30000);
        assertThat(ps4.isProVersion).isFalse();
        assertThat(ps4.currentOwner).isNull();
        assertThat(Arrays.stream(ps4.bestGames).toList())
                .contains("Detroit:_Become_Human", "Red_Dead_Redemption_2");
    }
}
