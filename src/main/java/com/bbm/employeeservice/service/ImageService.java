package com.bbm.employeeservice.service;

import com.bbm.employeeservice.exception.BusinessException;
import com.bbm.employeeservice.exception.EntityNotFoundException;
import com.bbm.employeeservice.model.Employee;
import com.bbm.employeeservice.model.Image;
import com.bbm.employeeservice.model.User;
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
    private final EmployeeService employeeService;

    public String upload(MultipartFile file, Long userId, Long employeeId) {
        User user = new User(userId);
        String contentType = file.getContentType();
        Employee employee = null;
        if (employeeId != null) {
            employee = employeeService.getEmployeeById(employeeId, userId);
        }
        boolean isAnImageFile = ImageUtils.isValidImageFile(contentType);
        if (isAnImageFile) {
            try {
                Image image = Image.builder()
                        .originalFileName(file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .image(file.getBytes())
                        .build();
                if (employee != null) {
                    image.setEmployee(employee);
                } else {
                    image.setUser(user);
                }
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
                new EntityNotFoundException("Não foi possível fazer o download da imagem!"));
    }

}
