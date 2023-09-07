package ru.plorum.reporterinitializr;

//import org.docx4j.Docx4J;
//import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
//import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
//import org.docx4j.wml.Text;

public class DocxTest {

    /*@Test
    void test1() throws Exception {
        final var wordPackage = WordprocessingMLPackage.createPackage();
        MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
        mainDocumentPart.addParagraphOfText("hello, {name}!");
        final var exportFile = new File("hello.docx");
        wordPackage.save(exportFile);
    }

    @Test
    void test2() throws Exception {
        final var doc = new File("hello.docx");
        final var wordMLPackage = WordprocessingMLPackage.load(doc);
        final var mainDocumentPart = wordMLPackage.getMainDocumentPart();
        final var textNodesXPath = "//w:t";
        final var textNodes = mainDocumentPart.getJAXBNodesViaXPath(textNodesXPath, true);
        for (final Object obj : textNodes) {
            final var text = (Text) ((JAXBElement) obj).getValue();
            final var textValue = text.getValue();
            if (textValue.contains("{name}")) {
                text.setValue(textValue.replace("{name}", "Тестов Тест Тестович"));
                wordMLPackage.save(doc);
            }
            System.out.println(textValue);
        }
    }*/

}
