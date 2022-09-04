import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import static com.codeborne.pdftest.assertj.Assertions.assertThat;
import static java.nio.charset.StandardCharsets.UTF_8;

public class TestFiles {
    ZipFile zf;
    Map<String, ZipEntry> entries = new HashMap<>();

    {
        try {
            zf = new ZipFile(new File("src/test/resources/Archive.zip"));
            var collection = zf.entries();
            Iterator iterator = collection.asIterator();
            while (iterator.hasNext()) {
                ZipEntry entry = (ZipEntry) iterator.next();
                entries.put(entry.getName(), entry);
            }
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("test csv content")
    public void csvTest() throws Exception {
        try (InputStream stream = zf.getInputStream(entries.get("turnover.csv"));
             CSVReader csvReader = new CSVReader(new InputStreamReader(stream, UTF_8))) {
            List<String[]> csv = csvReader.readAll();
            assertThat(csv).contains(
                    new String[]{"June", "5000", "Oleg"},
                    new String[]{"November", "16000", "Roman"}
            );
        }
    }

    @Test
    @DisplayName("test xls content")
    public void xlsTest() throws Exception {
        try (InputStream stream = zf.getInputStream(entries.get("managers_salary.xlsx"))) {
            XLS xls = new XLS(stream);
            assertThat(xls.excel.getSheet("Oleg's_salary")
                    .getRow(13)
                    .getCell(1)
                    .getNumericCellValue())
                    .isEqualTo(38000);
        }
    }

    @Test
    @DisplayName("test pdf content")
    public void pdfTest() throws Exception {
        try (InputStream stream = zf.getInputStream(entries.get("Part of article.pdf"))) {
            PDF pdf = new PDF(stream);
            String innerText = pdf.text.replaceAll("\r", "").replaceAll("\n", "");
            assertThat(innerText).containsIgnoringCase("в федеральных представительствах были продемонстрированы в Туманово");
            assertThat(innerText).containsIgnoringCase("Kudinov Roman and Petrov Ivan make a significant");
        }
    }
}
