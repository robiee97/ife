package com.ife.ife.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.*;

@Service
public class VideoProcessingService {

    private final Path SOURCE_DIR = Paths.get("/opt/app/_sources_videos");
    private final Path HLS_DIR = Paths.get("/opt/app/_hls_videos");
    private final Path THUMB_DIR = Paths.get("/opt/app/_thumbnails");

    public void processVideo(String fileName) {
        String movieName = fileName.replace(".mp4", "");
        Path sourceFile = SOURCE_DIR.resolve(fileName);
        Path movieHlsDir = HLS_DIR.resolve(movieName);
        Path thumbnailFile = THUMB_DIR.resolve(movieName + ".jpg");

        try {
            Files.createDirectories(movieHlsDir);
            Files.createDirectories(THUMB_DIR);

            // Skip already processed videos
            if (Files.exists(movieHlsDir.resolve("index.m3u8")) && Files.exists(thumbnailFile)) {
                System.out.println("Already processed: " + movieName);
                return;
            }

            System.out.println("Processing video: " + movieName);

            // HLS generation
            String[] hlsCommand = {
                    "ffmpeg", "-i", sourceFile.toString(),
                    "-profile:v", "baseline",
                    "-level", "3.0",
                    "-start_number", "0",
                    "-hls_time", "8",
                    "-hls_list_size", "0",
                    "-f", "hls",
                    movieHlsDir.resolve("index.m3u8").toString()
            };
            runCommand(hlsCommand, movieName, "HLS");

            // Thumbnail generation
            String[] thumbCommand = {
                    "ffmpeg", "-ss", "01:00:00",
                    "-i", sourceFile.toString(),
                    "-frames:v", "1",
                    "-q:v", "2",
                    thumbnailFile.toString()
            };
            runCommand(thumbCommand, movieName, "Thumbnail");

            System.out.println("Finished processing: " + movieName);

        } catch (Exception e) {
            System.err.println("Failed to process video: " + fileName);
            e.printStackTrace();
        }
    }

    private void runCommand(String[] command, String movieName, String step) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);
        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[" + movieName + "][" + step + "] " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException(step + " failed for video: " + movieName + " with exit code " + exitCode);
        }
    }
}
