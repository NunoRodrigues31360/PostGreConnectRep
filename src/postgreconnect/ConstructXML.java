package postgreconnect;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class ConstructXML {

    String file, operation, pkey;
    
    Element tabela,id,  oper, vts, pk, query;
    Document doc;
    File f;

    public ConstructXML(String fileName) throws TransformerException, SAXException, IOException {

        file = fileName;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            f = new File(file);
            Element rootElement;
            if (f.exists() && !f.isDirectory()) {
                doc = docBuilder.parse(file);
            } else {
                doc = docBuilder.newDocument();
                rootElement = doc.createElement("MYdbA");
                doc.appendChild(rootElement);
            }

            // elemento tabela
            tabela = doc.createElement("Tabela");
            id = doc.createElement("id");
            oper = doc.createElement("operacao");
            vts = doc.createElement("vts");
            pk = doc.createElement("pk"); //chave 
            query = doc.createElement("query");
            

        System.out.println("File saved!");
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ConstructXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void queryProcess(String command, String table, String pkey, String tableVersion) {
        this.pkey = pkey;

        if (command.substring(0, 1).equalsIgnoreCase("I")) {
            operation = "+";
        } else if (command.substring(0, 1).equalsIgnoreCase("U")) {
            operation = "*";
        } else //delete
        {
            operation = "-";
        }
        // elemento tabela
        tabela = doc.createElement("Tabela");
        doc.getFirstChild().appendChild(tabela);

        // atributo de tabela
        id = doc.createElement("id");
        oper = doc.createElement("operacao");
        vts = doc.createElement("vts");
        pk = doc.createElement("pk"); //chave 
        query = doc.createElement("query");
        id.appendChild(doc.createTextNode(table));
        oper.appendChild(doc.createTextNode(operation));
        vts.appendChild(doc.createTextNode(tableVersion));
        pk.appendChild(doc.createTextNode(pkey));
        query.appendChild(doc.createTextNode(command));
        tabela.appendChild(id);
        tabela.appendChild(oper);
        tabela.appendChild(vts);
        tabela.appendChild(pk);
        tabela.appendChild(query);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        try {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            Logger.getLogger(ConstructXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("File saved!");

    }
    
    static void eraseXMLFile(File file){
        file.delete();
    }
    
    public File getFile(){
        return f;
    }

}
