package com.ife.ife.runner;

import com.ife.ife.service.VideoProcessingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.*;

@Component
public class VideoStartupRunner implements CommandLineRunner {

    private final VideoProcessingService processor;

    public VideoStartupRunner(VideoProcessingService processor) {
        this.processor = processor;
    }

    @Override
    public void run(String... args) throws Exception {
        Path sourceDir = Paths.get("/opt/app/_sources_videos");

        if (!Files.exists(sourceDir)) {
            Files.createDirectory(sourceDir);
            return;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir, "*.mp4")) {
            for (Path file : stream) {
                processor.processVideo(file.getFileName().toString());
            }
        }
    }
}
