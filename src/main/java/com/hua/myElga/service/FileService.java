package com.hua.myElga.service;

import com.hua.myElga.entity.BreederSubmission;
import com.hua.myElga.entity.FileApplication;
import com.hua.myElga.entity.Submission;
import com.hua.myElga.repository.FileApplicationRepository;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class FileService {
    private static final Path SUBMISSION_PATH = Path.of("pdfSubmissions", "farmerApplication.pdf").toAbsolutePath().normalize();
    private static final Path BREEDER_PATH = Path.of("pdfSubmissions", "breederApplication.pdf").toAbsolutePath().normalize();
    private static final Path DEST = Path.of("pdfSubmissions", "tempFilledPdf.pdf").toAbsolutePath().normalize();

    @Autowired
    private FileApplicationRepository fileRepository;

    //// Get File attached to given application's id ////
    @Transactional
    public FileApplication getFile(Long id) {
        return fileRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find file with id: " + id));
    }

    //// Generate and store PDF of type as param ////
    @Transactional
    public FileApplication generateAndStorePdfFile(Submission application) throws IOException {
        // 1. Initialize file
        File file;

        // 2. Create appropriate file
        file = new File(String.valueOf(SUBMISSION_PATH));

        file.setWritable(false);

        // 3. Open the PDF template and create a writer for the destination file
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(file), new PdfWriter(String.valueOf(DEST)));

        // 4. Access the interactive form (AcroForm) in order to populate its fields
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        // 5. Set fields with given values
        Map<String, PdfFormField> fields = form.getAllFormFields();
        fields.get("id").setValue(String.valueOf(application.getId()));
        fields.get("name").setValue(application.getFarmer().getFirstName());
        fields.get("lastName").setValue(application.getFarmer().getLastName());
        fields.get("address").setValue(application.getFarmer().getAddress());
        fields.get("phone").setValue(String.valueOf(application.getFarmer().getPhoneNumber()));
        fields.get("afm").setValue(String.valueOf(application.getFarmer().getAFM()));
        fields.get("field").setValue(String.valueOf(application.getFieldArea()));
        fields.get("average").setValue(String.valueOf(application.getAverageProduction()));
        fields.get("percentage").setValue(String.valueOf(application.getDamagePercentage()));
        fields.get("price").setValue(String.valueOf(application.getPricePerUnit()));
        fields.get("date").setValue(application.getCreationDate());
        fields.get("damages").setValue(String.valueOf(application.getDamages().getDamagesSum()) + "€");

        // 6. Convert form fields into static content so they can no longer be edited
        form.flattenFields();

        // 7. Close the PDF document and finalize writing the output file
        pdfDoc.close();

        // 8. Store file to DB
        String filename = "application" + application.getId() + ".pdf";

        FileApplication fileApplication = new FileApplication(Files.readAllBytes(Paths.get(DEST.toString())), filename);

        FileApplication storedFile = fileRepository.save(fileApplication);

        // 9. Return File as FileApplication entity
        return storedFile;
    }

    @Transactional
    public FileApplication generateAndStorePdfFile(BreederSubmission application) throws IOException {
        // 1. Initialize file
        File file;

        // 2. Create appropriate file
        file = new File(String.valueOf(BREEDER_PATH));

        file.setWritable(false);

        // 3. Open the PDF template and create a writer for the destination file
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(file), new PdfWriter(String.valueOf(DEST)));

        // 4. Access the interactive form (AcroForm) in order to populate its fields
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        // 5. Set fields with given values
        Map<String, PdfFormField> fields = form.getAllFormFields();
        fields.get("id").setValue(String.valueOf(application.getId()));
        fields.get("name").setValue(application.getFarmer().getFirstName());
        fields.get("lastName").setValue(application.getFarmer().getLastName());
        fields.get("address").setValue(application.getFarmer().getAddress());
        fields.get("phone").setValue(String.valueOf(application.getFarmer().getPhoneNumber()));
        fields.get("afm").setValue(String.valueOf(application.getFarmer().getAFM()));
        fields.get("killed").setValue(String.valueOf(application.getNumberOfKilledAnimals()));
        fields.get("total").setValue(String.valueOf(application.getTotalAnimals()));
        fields.get("factor").setValue(String.valueOf(application.getCompensationFactor()));
        fields.get("residual").setValue(String.valueOf(application.getResidualValue()));
        fields.get("percentage").setValue(String.valueOf(application.getDamageCoveragePercentage()));
        fields.get("price").setValue(String.valueOf(application.getPricePerUnit()));
        fields.get("date").setValue(application.getCreationDate());
        fields.get("damages").setValue(String.valueOf(application.getDamages().getDamagesSum()) + "€");

        // 6. Convert form fields into static content so they can no longer be edited
        form.flattenFields();

        // 7. Close the PDF document and finalize writing the output file
        pdfDoc.close();

        // 8. Store file to DB
        String filename = "application" + application.getId() + ".pdf";

        FileApplication fileApplication = new FileApplication(Files.readAllBytes(Paths.get(DEST.toString())), filename);

        FileApplication storedFile = fileRepository.save(fileApplication);

        // 9. Return File as FileApplication entity
        return storedFile;
    }
    //// EOF ////
}
