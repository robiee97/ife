package com.ife.ife.controller;

import com.ife.ife.service.VideoProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class VideoUploadController {

    private final Path SOURCE_DIR = Paths.get("/opt/app/_sources_videos");
    private final Path HLS_DIR = Paths.get("/opt/app/_hls_videos");
    private final Path THUMB_DIR = Paths.get("/opt/app/_thumbnails");

    @Autowired
    private VideoProcessingService videoProcessingService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file) throws Exception {

        if (file.isEmpty()) return ResponseEntity.badRequest().body("File is empty");
        if (!file.getOriginalFilename().endsWith(".mp4"))
            return ResponseEntity.badRequest().body("Only MP4 allowed");

        String fileName = file.getOriginalFilename();
        Path targetFile = SOURCE_DIR.resolve(fileName);

        Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

        // 🔥 Use SAME processing logic
        videoProcessingService.processVideo(fileName);

        Map<String, String> result = new HashMap<>();
        String name = fileName.replace(".mp4", "");

        result.put("movie", name);
        result.put("hls", "/hls/" + name + "/index.m3u8");
        result.put("thumbnail", "/thumbnails/" + name + ".jpg");

        return ResponseEntity.ok(result);
    }

}
