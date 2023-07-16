package com.bbm.employeeservice.service;

import com.bbm.employeeservice.exception.BusinessException;
import com.bbm.employeeservice.model.Image;
import com.bbm.employeeservice.repository.ImageRepository;
import com.bbm.employeeservice.utils.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public String upload(MultipartFile file) {
        String contentType = file.getContentType();
        boolean isAnImageFile = ImageUtils.isValidImageFile(contentType);
        if (isAnImageFile) {
            try {
                Image image = Image.builder()
                        .originalFileName(file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .image(file.getBytes())
                        .build();
                imageRepository.save(image);
                return "Imagem foi Carregada com Sucesso: " + file.getOriginalFilename();
            } catch (Exception e) {
                throw new BusinessException("Não foi possível salvar a Imagem: " + file.getOriginalFilename() +
                        "\nOcorreu um erro interno. Por favor tente novamente. Caso o erro persista contacte o ADMIN!");
            }
        } else
            throw new BusinessException("Tipo de ficheiro não é válido!");
    }

    @Transactional
    public Image download(String fileName) {
        return imageRepository.findByOriginalFileName(fileName).orElseThrow(() ->
                new BusinessException("Não foi possível fazer o download da imagem"));
    }

}
