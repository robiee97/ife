package com.ife.ife.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VideoController {
    private final String VIDEO_DIR = "/opt/videos";

    @GetMapping("/")
    public String helloPage(Model model) {
        File folder = new File(VIDEO_DIR);
        File[] videoFolders = folder.listFiles(File::isDirectory);

        List<Map<String, String>> videos = new ArrayList<>();
        if (videoFolders != null) {
            for (File vidFolder : videoFolders) {
                Map<String, String> videoData = new HashMap<>();
                videoData.put("name", vidFolder.getName());
                // Check if thumbnail exists
                File thumb = new File(vidFolder, "thumb.jpg");
                videoData.put("thumbnail", thumb.exists() ? "/videos/" + vidFolder.getName() + "/thumb.jpg" : "/placeholder.jpg");
                videos.add(videoData);
            }
        }
        model.addAttribute("videos", videos);
        return "hello";
    }
}
