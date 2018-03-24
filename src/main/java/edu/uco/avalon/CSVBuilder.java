package edu.uco.avalon;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CSVBuilder {

    public static InputStream create(CellProcessor[] processors, final String[] headers, List items) throws IOException {
        ICsvBeanWriter beanWriter = null;
        StringWriter writer = new StringWriter();
        try {
            beanWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);

            // write the header
            beanWriter.writeHeader(headers);

            // write the beans
            for (final Object element : items) {
                beanWriter.write(element, headers, processors);
            }

        } finally {
            if (beanWriter != null) {
                beanWriter.close();
            }
        }
        InputStream stream = new ByteArrayInputStream(writer.toString().getBytes(StandardCharsets.UTF_8));
        return stream;
    }

    public static String filename(final String slice) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String filename = dateFormat.format(new Date()) + "-avalon-" + slice + ".csv";
        return filename;
    }
}
