package com.example.sennova.infrastructure.restTemplate;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile image) {
        try {
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map deleteFileByUrl(String url) throws IOException {
        String publicId = extractPublicIdFromUrl(url);
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    private String extractPublicIdFromUrl(String url) {

        String[] parts = url.split("/upload/");
        if (parts.length < 2) {
            throw new IllegalArgumentException("URL inválida de Cloudinary: " + url);
        }

        String path = parts[1];


        if (path.startsWith("v")) {
            path = path.substring(path.indexOf("/") + 1);
        }


        int dotIndex = path.lastIndexOf(".");
        if (dotIndex > 0) {
            path = path.substring(0, dotIndex);
        }

        return path;
    }

    public Map<String, String> uploadFile(MultipartFile file) {
        try {
            Map<String, String> mapResultDto = new HashMap<>();

            String originalFilename = file.getOriginalFilename();
            String baseName = originalFilename != null
                    ? originalFilename.replaceAll("\\.[^.]*$", "")
                    : "archivo";


            String uniquePublicId = baseName + "_" + System.currentTimeMillis();

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "raw",
                            "public_id", uniquePublicId,
                            "overwrite", true
                    )
            );

            mapResultDto.put("secure_url", uploadResult.get("secure_url").toString());
            mapResultDto.put("public_id", uploadResult.get("public_id").toString());
            mapResultDto.put("contentType", file.getContentType());
            mapResultDto.put("originalFilename", originalFilename);
            return mapResultDto;

        } catch (IOException e) {
            throw new RuntimeException("Error al subir archivo a Cloudinary", e);
        }
    }

    public void deleteFile(String publicId) {
        try {

            Map result = cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap(
                            "resource_type", "raw"
                    )
            );


            if (!"ok".equals(result.get("result"))) {
                throw new RuntimeException("No se pudo eliminar el archivo en Cloudinary: " + publicId);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar archivo en Cloudinary", e);
        }
    }


}
