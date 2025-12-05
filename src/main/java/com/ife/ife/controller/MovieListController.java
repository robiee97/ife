package com.ife.ife.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class MovieListController {

    private final Path SOURCE_DIR = Paths.get("/opt/app/_sources_videos");

    @GetMapping("/movies")
    public List<Map<String, String>> listMovies() throws IOException {

        List<Map<String, String>> movies = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(SOURCE_DIR, "*.mp4")) {
            for (Path file : stream) {

                String name = file.getFileName().toString().replace(".mp4", "");

                Map<String, String> movie = new HashMap<>();
                movie.put("title", name);
                movie.put("thumbnail", "/thumbnails/" + name + ".jpg");
                movie.put("streamUrl", "/hls/" + name + "/index.m3u8");

                movies.add(movie);
            }
        }

        return movies;
    }
}
