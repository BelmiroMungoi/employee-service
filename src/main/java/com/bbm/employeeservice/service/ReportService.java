package com.bbm.employeeservice.service;

import com.bbm.employeeservice.exception.BusinessException;
import com.bbm.employeeservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.sql.Connection;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final EmployeeRepository employeeRepository;
    private final JdbcTemplate jdbcTemplate;

    public byte[] generateReport(Map<String, Object> params) {
        try {
            //List<Employee> employees = employeeRepository.findAll();
            //JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            String file = ResourceUtils.getFile("classpath:" + "webapp" + File.separator + "reports"
                    + File.separator + "EmployeeRep.jasper").getAbsolutePath();
            //JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JasperPrint print = JasperFillManager.fillReport(file, params, connection);
            return JasperExportManager.exportReportToPdf(print);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new BusinessException("Não foi possível gerar o relatório. Ocorreu um erro interno, " +
                    "por favor Tente Novamente. Caso o erro persista contacte o ADMIN");
        }
    }
}
