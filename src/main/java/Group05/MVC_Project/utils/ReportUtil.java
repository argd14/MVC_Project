package Group05.MVC_Project.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Group05.MVC_Project.models.Issue;
import Group05.MVC_Project.models.Response;
import Group05.MVC_Project.models.User;
import Group05.MVC_Project.repositories.IssueRepository;
import Group05.MVC_Project.repositories.UserRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class ReportUtil {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IssueRepository issueRepository;
    private Response response;

    public Response exportReport(String reportFormat, Long id) throws FileNotFoundException, JRException {
        initializeResponse();
        String path = "C:\\Users\\angel\\IdeaProjects\\ProjectGroup05\\MVC_Project";
        List<Issue> issues = issueRepository.issuesByIdUser(id);
        File file = ResourceUtils.getFile("classpath:reportUsers.jrxml");
        JasperDesign jasperDesign;
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(issues);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java project MVC");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\users.pdf");
            JasperExportManager.exportReportToXmlFile(jasperPrint,path + "\\usersXL.pdf",true);
          /*  JRXlsExporter exporter = new JRXlsExporter();
            ArrayList<JasperPrint> sheets = new ArrayList<JasperPrint>();
            sheets.add(jasperPrint);
            exporter.setExporterInput(SimpleExporterInput.getInstance(sheets));*/
            response.setMessage("PDF report generated");
            response.setStatus(true);
        }
        return response;
    }
    public void initializeResponse() {
        this.response = new Response();
    }

}
