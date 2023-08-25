package com.bbm.employeeservice.service;

import com.bbm.employeeservice.exception.BusinessException;
import com.bbm.employeeservice.exception.EntityNotFoundException;
import com.bbm.employeeservice.model.*;
import com.bbm.employeeservice.model.dto.*;
import com.bbm.employeeservice.repository.DepartmentRepository;
import com.bbm.employeeservice.repository.EmployeeRepository;
import com.bbm.employeeservice.repository.EmployeeSearchDao;
import com.bbm.employeeservice.repository.PositionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeSearchDao employeeSearch;
    private final DepartmentService departmentService;
    private final AddressService addressService;
    private final MissionService missionService;
    private final ImageService imageService;
    private final JdbcTemplate jdbcTemplate;
    private final ResourceLoader resourceLoader;

    public AppResponse createEmployee(EmployeeRequest employeeRequest, MultipartFile file, Long userId) {
        if (employeeRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new BusinessException("Já existe um funcionário registrado com esse Email");
        }

        User user = new User(userId);
        Address saveAddress = addressService.saveAddress(employeeRequest);
        Position position = positionRepository.findByPositionName(employeeRequest.getPosition());
        Department getDepartment = departmentService.getDepartmentByName(employeeRequest.getDepartment(), userId);
        getDepartment.setEmployeeQuantity(getDepartment.getEmployeeQuantity() + 1);

        Employee employee = Employee.builder()
                .employeeIdentifier(UUID.randomUUID().toString())
                .firstname(employeeRequest.getFirstname())
                .lastname(employeeRequest.getLastname())
                .email(employeeRequest.getEmail())
                .birthdate(employeeRequest.getBirthdate())
                .salary(employeeRequest.getSalary())
                .address(saveAddress)
                .department(getDepartment)
                .position(position)
                .role(Role.USER)
                .user(user)
                .build();
        employeeRepository.save(employee);

        getDefaultPic(employee.getId(), userId);
        return AppResponse.builder()
                .responseCode("201")
                .responseMessage("Funcionário foi Salvo com Sucesso")
                .name(employee.getFirstname() + " " + employee.getLastname())
                .build();
    }

    @Transactional
    public Page<EmployeeResponse> getEmployees(Long userId) {
        PageRequest pageRequest = PageRequest.of(0, 8, Sort.by("id"));
        Page<Employee> employees = employeeRepository.findAllByUserId(pageRequest, userId);

        return employees.map(this::mapToEmployeeResponse);
    }

    @Transactional
    public Page<EmployeeResponse> getEmployeesPerPage(int page, Long userId) {
        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by("id"));
        Page<Employee> employees = employeeRepository.findAllByUserId(pageRequest, userId);

        return employees.map(this::mapToEmployeeResponse);
    }

    @Transactional
    public Employee getEmployeeById(Long employeeId, Long userId) {
        return employeeRepository.findByIdAndUserId(employeeId, userId).orElseThrow(() ->
                new EntityNotFoundException("Funcionário com ID: " + employeeId + " não foi encontrado."));
    }

    @Transactional
    public Page<EmployeeResponse> getEmployeeByFirstname(String firstname, Long userId) {
        PageRequest pageRequest = PageRequest.of(0, 8, Sort.by("id"));
        Page<Employee> employees = employeeRepository.findAllByFirstnameContainsIgnoreCaseAndUserId(pageRequest, firstname, userId).orElseThrow(() ->
                new EntityNotFoundException("Funcionário com o nome: " + firstname + " não foi encontrado"));
        return employees.map(this::mapToEmployeeResponse);
    }

    @Transactional
    public Page<EmployeeResponse> getEmployeeByFirstnamePerPage(int page, String firstname, Long userId) {
        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by("id"));
        Page<Employee> employees = employeeRepository.findAllByFirstnameContainsIgnoreCaseAndUserId(pageRequest, firstname, userId).orElseThrow(() ->
                new EntityNotFoundException("Funcionário com o nome: " + firstname + " não foi encontrado"));
        return employees.map(this::mapToEmployeeResponse);
    }

    @Transactional
    public List<EmployeeResponse> getAllEmployeesByDepartment(String departmentName, Long userId) {
        List<Employee> employees = employeeRepository.findAllByDepartmentNameAndUserId(departmentName, userId);

        return employees.stream().map(this::mapToEmployeeResponse).toList();
    }

    @Transactional
    public AppResponse updateEmployee(Long employeeId, EmployeeRequest employeeRequest, Long userId) {
        Department getDepartment = departmentService.getDepartmentByName(employeeRequest.getDepartment(), userId);
        getDepartment.setEmployeeQuantity(getDepartment.getEmployeeQuantity() + 1);
        Set<Mission> getMission = missionService.getMissionByName(employeeRequest.getMission(), userId);
        Position position = positionRepository.findByPositionName(employeeRequest.getPosition());

        Employee employee = getEmployeeById(employeeId, userId);
        employee.getDepartment().setEmployeeQuantity(employee.getDepartment().getEmployeeQuantity() - 1);
        departmentRepository.save(employee.getDepartment());
        Address updateAddress = addressService.updateAddress(employee.getAddress().getId(), employeeRequest);
        employee.setFirstname(employeeRequest.getFirstname());
        employee.setLastname(employeeRequest.getLastname());
        employee.setEmail(employeeRequest.getEmail());
        employee.setBirthdate(employeeRequest.getBirthdate());
        employee.setSalary(employeeRequest.getSalary());
        employee.setAddress(updateAddress);
        employee.setDepartment(getDepartment);
        employee.setMissions(getMission);
        employee.setPosition(position);
        employeeRepository.save(employee);

        return AppResponse.builder()
                .responseCode("200")
                .responseMessage("Funcionário foi actualizado com sucesso")
                .name(employee.getFirstname() + " " + employee.getLastname())
                .build();
    }

    @Transactional
    public void deleteEmployee(Long employeeId, Long userId) {
        Employee employee = getEmployeeById(employeeId, userId);
        employee.getDepartment().setEmployeeQuantity(employee.getDepartment().getEmployeeQuantity() - 1);
        departmentRepository.save(employee.getDepartment());
        employeeRepository.delete(employee);
    }

    @Transactional
    public List<EmployeeResponse> searchAllEmployees(String firstname, String lastname, String email) {
        List<Employee> employees = employeeSearch.findAllBySimpleQuery(firstname, lastname, email);

        return employees.stream().map(this::mapToEmployeeResponse).toList();
    }

    @Transactional
    public List<EmployeeResponse> searchAllEmployeesByName(SearchRequest request) {
        List<Employee> employees = employeeSearch.findAllByCriteria(request);

        return employees.stream().map(this::mapToEmployeeResponse).toList();
    }

    public Integer getEmployeeQuantityByUser(Long userId) {
        return employeeRepository.countAllByUserId(userId);
    }

    public List<PositionResponse> getAllPosition() {
        List<Position> position = positionRepository.findAll();
        return position.stream().map(this::mapToPositionResponse).toList();
    }

    @Transactional
    public Page<EmployeeResponse> getAllEmployeeByMissionId(Long missionId, Long userId, int page) {
        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by("id"));
        Page<Employee> employees = employeeRepository.findAllByMissionIdAndUserId(pageRequest, missionId, userId);
        return employees.map(this::mapToEmployeeResponse);
    }

    @Transactional
    public Page<EmployeeResponse> getAllEmployeeWithoutThatMission(Long missionId, Long userId, int page) {
        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by("id"));
        Page<Employee> employees = employeeRepository.findAllEmployeeWithoutThatMission(pageRequest, missionId, userId);
        return employees.map(this::mapToEmployeeResponse);
    }

    public UserChartResponse generateChart() {
        UserChartResponse userChart = new UserChartResponse();

        List<String> result = jdbcTemplate.queryForList(
                "select array_agg(firstname) from employee where salary > 0 and firstname <> ''" +
                        "union all select cast(array_agg(salary) as character varying[])" +
                        "from employee where salary > 0 and firstname <> ''",
                String.class
        );
        if (!result.isEmpty()) {
            String firstname = result.get(0).replaceAll("\\{", "").replaceAll("}", "");
            String salary = result.get(1).replaceAll("\\{", "").replaceAll("}", "");

            userChart.setFirstname(firstname);
            userChart.setSalary(salary);
        }

        return userChart;
    }

    @Transactional
    public AppResponse addMissionToEmployee(Long missionId, Long employeeId, Long userId) {
        Employee employee = getEmployeeById(employeeId, userId);
        Mission mission = missionService.getMissionById(missionId, userId);
        if (mission.getEmployees().contains(employee)) {
            throw new BusinessException("Este funcionário já está alocado á esse projecto");
        }
        employee.addMission(mission);
        mission.addEmployee(employee);
        employeeRepository.save(employee);

        return AppResponse.builder()
                .responseCode("200")
                .responseMessage("Funcionário foi alocado para o projecto com sucesso!")
                .name(employee.getFirstname() + " " + employee.getLastname())
                .build();
    }

    @Transactional
    public AppResponse addImageToEmployee(MultipartFile file, Long employeeId, Long userId) {
        Employee employee = getEmployeeById(employeeId, userId);
        if (!file.isEmpty()) {
            Image image = imageService.upload(file, null);
            employee.setImage(image);
            employeeRepository.save(employee);
        }
        return AppResponse.builder()
                .responseCode("200")
                .responseMessage("Imagem foi actualizada com sucesso!")
                .name(employee.getFirstname() + " " + employee.getLastname())
                .build();
    }

    public void getDefaultPic(Long employeeId, Long userId) {
        try {
            Resource pathResource = resourceLoader.getResource("classpath:img" + File.separator + "default-profile.png");
            //File file = ResourceUtils.getFile("classpath:" + "img" + File.separator + "default-profile.png");
            InputStream input = pathResource.getInputStream();
            MultipartFile multipartFile = new MockMultipartFile("file",
                    pathResource.getFilename(), MediaType.IMAGE_PNG_VALUE, IOUtils.toByteArray(input));
            addImageToEmployee(multipartFile, employeeId, userId);
        } catch (IOException fe) {
            System.err.println(fe.getMessage());
            throw new BusinessException("Não encontrado");
        }
    }

    public EmployeeResponse mapToEmployeeResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .employeeIdentifier(employee.getEmployeeIdentifier())
                .firstname(employee.getFirstname())
                .lastname(employee.getLastname())
                .email(employee.getEmail())
                .birthdate(employee.getBirthdate())
                .role(employee.getRole())
                .salary(employee.getSalary())
                .department(DepartmentResponse.builder()
                        .id(employee.getDepartment().getId())
                        .name(employee.getDepartment().getName())
                        .shortName(employee.getDepartment().getShortName())
                        .build())
                .positionResponse(PositionResponse.builder()
                        .positionName(employee.getPosition().getPositionName())
                        .build())
                .address(AddressResponse.builder()
                        .houseNumber(employee.getAddress().getHouseNumber())
                        .street(employee.getAddress().getStreet())
                        .zipCode(employee.getAddress().getZipCode())
                        .build())
                .imageResponse(ImageResponse.builder()
                        .image(ImageService.decompressBytes(employee.getImage().getImage()))
                        .originalFileName(employee.getImage().getOriginalFileName())
                        .fileType(employee.getImage().getFileType())
                        .build())
                .build();
    }

    public PositionResponse mapToPositionResponse(Position position) {
        return PositionResponse.builder()
                .positionName(position.getPositionName())
                .build();
    }
}
