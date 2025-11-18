package com.likelion.mooding.common.compress;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompressDebugController {

    private final String response;

    public CompressDebugController(final ResourceLoader resourceLoader) {
        final Resource resource = resourceLoader.getResource("classpath:search-result.json");
        try (final InputStream is = resource.getInputStream()) {
            this.response = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @GetMapping("/api/feedback/debug")
    public ResponseEntity<String> getJson() {
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(response);
    }
}
