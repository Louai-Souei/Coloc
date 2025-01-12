package CU.projet.ColocationUniversitaire.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class ImageController {

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/images/";

    @GetMapping("/images/{imageName}")
    public ResponseEntity<FileSystemResource> getImage(@PathVariable String imageName) {
        File file = new File(IMAGE_DIRECTORY + imageName);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        FileSystemResource fileSystemResource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(fileSystemResource);
    }
}
