package com.example.demo.service;

import com.example.demo.domain.Person;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {
    @Value("./uploads")
    private String uploadDir;
    public String getPathInUploadDir(String fileName) {
        return Paths.get(uploadDir).resolve(fileName).toString();
    }

    public String saveFile(MultipartFile file) throws IOException {
        // Clean up the file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Validate file format (only JPG or PNG allowed)
        if (!fileName.toLowerCase().endsWith(".jpg") && !fileName.toLowerCase().endsWith(".png")) {
            throw new IllegalArgumentException("Only JPG or PNG files are allowed");
        }

        // Generate a unique file name
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        Path targetLocation = Paths.get(uploadDir).resolve(uniqueFileName);

        // Resize and optimize the image
        Thumbnails.of(file.getInputStream())
                .size(300, 400) // Set dimensions
                .outputFormat(getFileExtension(fileName)) // Maintain the format
                .outputQuality(0.8)
                .toFile(targetLocation.toFile());

        return fileName;
    }
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            }
            throw new RuntimeException("Plik nie znaleziony " + fileName);
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Plik nie znaleziony " + fileName);
        }
    }
    public void deleteFile(String fileName) {
        try {
            Path path = Paths.get(uploadDir).resolve(fileName).normalize();
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + fileName, e);
        }
    }
    public Path exportPhotosToZip(String fileNameFormat, List<Person> employees) throws IOException {
        // Save the ZIP file in the upload directory
        Path zipFilePath = Paths.get(uploadDir).resolve("employee_photos.zip");

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {
            for (Person employee : employees) {
                if (!employee.getImagePath().isEmpty()) {
                    Path originalImagePath = Paths.get(uploadDir).resolve(employee.getImagePath()).normalize();
                    String imageExtension = getFileExtension(employee.getImagePath());
                    String formattedFileName = fileNameFormat
                            .replace("[id]", String.valueOf(employee.getId()))
                            .replace("[surname]", employee.getLastName()) + "." + imageExtension;

                    // Add image file to ZIP
                    zipOutputStream.putNextEntry(new ZipEntry(formattedFileName));
                    Files.copy(originalImagePath, zipOutputStream);
                    zipOutputStream.closeEntry();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to export employee photos", e);
        }
        return zipFilePath;
    }

}