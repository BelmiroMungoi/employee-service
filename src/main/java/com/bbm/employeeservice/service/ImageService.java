package com.bbm.employeeservice.service;

import com.bbm.employeeservice.exception.BusinessException;
import com.bbm.employeeservice.exception.EntityNotFoundException;
import com.bbm.employeeservice.model.Image;
import com.bbm.employeeservice.model.User;
import com.bbm.employeeservice.repository.ImageRepository;
import com.bbm.employeeservice.utils.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final ImageRepository imageRepository;

    public Image upload(MultipartFile file, Long userId) {
        User user = new User(userId);
        String contentType = file.getContentType();
        boolean isAnImageFile = ImageUtils.isValidImageFile(contentType);
        if (isAnImageFile) {
            try {
                Image image = Image.builder()
                        .originalFileName(file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .image(compressBytes(file.getBytes()))
                        .user(user)
                        .build();
                log.info("Imagem foi Carregada com Sucesso");
                return imageRepository.save(image);
            } catch (Exception e) {
                throw new BusinessException("Não foi possível salvar a Imagem: " + file.getOriginalFilename() +
                        "\nOcorreu um erro interno. Por favor tente novamente. Caso o erro persista contacte o ADMIN!");
            }
        } else
            throw new BusinessException("Tipo de ficheiro não é válido!");
    }

    @Transactional
    public Image download(Long userId) {
        var img = imageRepository.findByUserId(userId).orElseThrow(() ->
                new EntityNotFoundException("Não foi possível fazer o download da imagem!"));
        return Image.builder()
                .id(img.getId())
                .originalFileName(img.getOriginalFileName())
                .fileType(img.getFileType())
                .image(decompressBytes(img.getImage()))
                .build();
    }

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            throw new BusinessException("Erro ao Salvar Imagem");
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException ioe) {
            System.out.println(ioe.getMessage());
            throw new BusinessException("Ocorreu um erro interno ao carregar a imagem");
        }
        return outputStream.toByteArray();
    }

}
