package com.senimen.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.senimen.models.Certificate;
import com.senimen.models.Event;
import com.senimen.models.User;
import com.senimen.repositories.CertificateRepository;
import com.senimen.repositories.EventRepository;
import com.senimen.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class CertificateService {
    private final CertificateRepository certRepo;
    private final EventRepository eventRepo;
    private final UserRepository userRepo;

    public CertificateService(CertificateRepository certRepo, EventRepository eventRepo, UserRepository userRepo) {
        this.certRepo = certRepo; this.eventRepo = eventRepo; this.userRepo = userRepo;
    }

    public Certificate generateFor(String eventId, String volunteerId){
        Event e = eventRepo.findById(eventId).orElseThrow();
        User u = userRepo.findById(volunteerId).orElseThrow();
        try{
            File dir = new File("data/certificates"); if(!dir.exists()) dir.mkdirs();
            Certificate c = new Certificate(); c.setEventId(eventId); c.setVolunteerId(volunteerId);
            String filename = "cert_"+volunteerId+"_"+eventId+".pdf";
            File out = new File(dir, filename);
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(out));
            doc.open();
            Font title = new Font(Font.HELVETICA, 22, Font.BOLD);
            Font normal = new Font(Font.HELVETICA, 14);
            doc.add(new Paragraph("Certificate of Participation", title));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("This is to certify that "+u.getName()+" participated in the event '" + e.getTitle() + "'", normal));
            doc.add(new Paragraph("on "+ DateTimeFormatter.ISO_INSTANT.format(e.getDate())+".", normal));
            doc.add(new Paragraph("Location: "+(e.getLocation()!=null?e.getLocation().getCity()+", "+e.getLocation().getAddress():""), normal));
            doc.add(new Paragraph("Role: Volunteer", normal));
            doc.close();
            c.setPdfPath(out.getAbsolutePath());
            return certRepo.save(c);
        }catch(Exception ex){ throw new RuntimeException(ex); }
    }
}
