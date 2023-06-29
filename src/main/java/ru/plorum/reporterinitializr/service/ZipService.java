package ru.plorum.reporterinitializr.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import ru.plorum.reporterinitializr.component.ErrorNotification;

import java.io.*;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class ZipService {

    public InputStream getZipInputStream(final Map<String, Object> props, final String postfix) {
        try (
                final var outputStream = new ByteArrayOutputStream();
                final var printWriter = new PrintWriter(outputStream, true);
                final var zipOutputStream = new ByteArrayOutputStream();
                final var zipOut = new ZipOutputStream(zipOutputStream)
        ) {
            props.forEach((k, v) -> printWriter.println(String.format("%s=%s", k, v)));
            final var propertiesInputStream = new ByteArrayInputStream(outputStream.toByteArray());
            final var propertiesEntry = new ZipEntry("application.properties");
            zipOut.putNextEntry(propertiesEntry);
            IOUtils.copy(propertiesInputStream, zipOut);
            propertiesInputStream.close();
            final var jarInputStream = new FileInputStream(String.format("packs/reporter-%s.jar", postfix));
            final var jarEntry = new ZipEntry(String.format("reporter-%s.jar", postfix));
            zipOut.putNextEntry(jarEntry);
            IOUtils.copy(jarInputStream, zipOut);
            jarInputStream.close();
            zipOut.close();

            return new ByteArrayInputStream(zipOutputStream.toByteArray());
        } catch (Exception e) {
            log.error("error downloading zip", e);
            new ErrorNotification("Ошибка формирования сборки");
            return InputStream.nullInputStream();
        }
    }

}
