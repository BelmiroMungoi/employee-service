package com.bbm.employeeservice.controller;

import com.bbm.employeeservice.model.Image;
import com.bbm.employeeservice.model.User;
import com.bbm.employeeservice.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImageForUser(@RequestParam("file") MultipartFile file,
                                    @AuthenticationPrincipal User authenticatedUser){
        return new ResponseEntity<>(imageService.upload(file, authenticatedUser.getId(), null), HttpStatus.OK);
    }
    @PostMapping("/upload/{employeeId}")
    public ResponseEntity<?> uploadImageForEmployee(@RequestParam("file") MultipartFile file,
                                    @AuthenticationPrincipal User authenticatedUser,
                                    @PathVariable("employeeId") Long employeeId){
        return new ResponseEntity<>(imageService.upload(file, authenticatedUser.getId(), employeeId), HttpStatus.OK);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadImage(@PathVariable("fileName") String name) {
        Image image = imageService.download(name);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + image.getOriginalFileName() + "\"")
                .body(new ByteArrayResource(image.getImage()));
    }
}
