package com.bbm.employeeservice.controller;

import com.bbm.employeeservice.model.Image;
import com.bbm.employeeservice.model.User;
import com.bbm.employeeservice.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> uploadImageForUser(@RequestParam("file") MultipartFile file,
                                    @AuthenticationPrincipal User authenticatedUser){
        imageService.upload(file, authenticatedUser.getId());
        return new ResponseEntity<>("Imagem foi actualizada com sucesso!", HttpStatus.OK);
    }

    @GetMapping("/download/")
    public ResponseEntity<Image> downloadImage(@AuthenticationPrincipal User authenticatedUser) {
        Image image = imageService.download(authenticatedUser.getId());
        return ResponseEntity.ok(image);
        /*return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + image.getOriginalFileName() + "\"")
                .body(new ByteArrayResource(image.getImage()));*/
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteImage(@AuthenticationPrincipal User authenticatedUser) {
        imageService.delete(authenticatedUser.getId());
        return new ResponseEntity<>("Imagem eliminada com sucesso!", HttpStatus.OK);
    }
}
