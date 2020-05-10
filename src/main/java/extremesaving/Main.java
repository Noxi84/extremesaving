package extremesaving;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import extremesaving.pdf.facade.PdfFacade;

public class Main {

    private PdfFacade pdfFacade;

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath*:/applicationContext*.xml")
                .getBean(Main.class)
                .start();
    }

    private void start() {
        System.out.println("Generating pdf...");
        pdfFacade.generatePdf();
        System.out.println("Done.");
    }

    public void setPdfFacade(PdfFacade pdfFacade) {
        this.pdfFacade = pdfFacade;
    }
}