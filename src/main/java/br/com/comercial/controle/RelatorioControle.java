/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;

import br.com.comercial.entidade.Emissao;
import br.com.comercial.entidade.Emissao65;
import br.com.comercial.util.ReportsUtil;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.sf.jasperreports.engine.JRException;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author JW Junior
 */
@Named
@RequestScoped
public class RelatorioControle implements Serializable {

    @Inject
    private EntityManager em;
    private Emissao65 emissao;

    public void geraDanfeNfce(Emissao65 e) throws IOException {

        String fileXML = "C:\\Users\\JW Junior\\Desktop\\xml\\" + e.getChave() + ".xml";
        String xml = lerXML(fileXML);
        String logo = "C:\\Uploads\\nfe.png";

        byte[] pdf = getDanfe(xml, logo);

        String nomeArquivo = "C:\\Users\\JW Junior\\Desktop\\xml\\xml.pdf";
        FileOutputStream fos = new FileOutputStream(nomeArquivo);
        fos.write(pdf);
        fos.close();

    }

    private static String lerXML(String fileXML) throws IOException {
        String linha = "";
        StringBuilder xml = new StringBuilder();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileXML)));
        while ((linha = in.readLine()) != null) {
            xml.append(linha);
        }
        in.close();

        return xml.toString();
    }

    public static Document getXml(String xml) {
        try {
            /**
             * Gera um objeto DOM do xml
             */
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            return docBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
        } catch (Exception e) {
            error("Erro ao formatar a string em xml.");
            return null;
        }
    }

    public static Map<String, Object> getFaturas(Document doc) {
        Map<String, Object> map = new HashMap<String, Object>();

        for (int i = 0; i < doc.getElementsByTagName("dup").getLength(); i++) {
            /**
             * Seta uma duplicata
             */
            Node dup = doc.getElementsByTagName("dup").item(i);

            /**
             * Seta o numero
             */
            Node nDoc = dup.getChildNodes().item(0);
            String numero = nDoc.getFirstChild().getNodeValue();
            map.put("FAT_NUMERO" + (i + 1), numero);

            /**
             * Seta a data
             */
            Node nData = dup.getChildNodes().item(1);
            String data = nData.getFirstChild().getNodeValue();

            try {
                map.put("FAT_VENCIMENTO" + (i + 1), new SimpleDateFormat("yyyy-MM-dd").parse(data));
            } catch (Exception e) {
                map.put("FAT_VENCIMENTO" + (i + 1), null);

            }

            /**
             * Seta o valor
             */
            Node nValor = dup.getChildNodes().item(2);
            String valor = nValor.getFirstChild().getNodeValue();
            map.put("FAT_VALOR" + (i + 1), Double.parseDouble(valor));
        }
        return map;
    }

    public static byte[] getDanfe(String xml, String logo) {
        byte[] pdf = null;
        try {
            /**
             * Documento XML.
             */
            Document doc = getXml(xml);

            String tipoImp = doc.getElementsByTagName("tpImp").item(0).getFirstChild().getNodeValue();
            /**
             * Local do relatorio jrxml
             */
            String urlDanfe = tipoImp.equals("4") ? "C:\\Users\\JW Junior\\Downloads\\DanfeNFCe.jrxml" : "C:\\Users\\JW Junior\\Downloads\\DanfeNFCe.jrxml";
            JasperDesign jasperDesign = JRXmlLoader.load(urlDanfe);
            JasperReport jasper = JasperCompileManager.compileReport(jasperDesign);

            /**
             * Fonte de Dados.
             */
            JRXmlDataSource ds = new JRXmlDataSource(doc, "/nfeProc/NFe/infNFe/det");

            /**
             * Parametros
             */
            Map<String, Object> param = getFaturas(doc);
            param.put("Logo", logo);

            /**
             * Gerando o relatorio
             */
            JasperPrint print = JasperFillManager.fillReport(jasper, param, ds);

            /**
             * Exportando em pdf
             */
            pdf = JasperExportManager.exportReportToPdf(print);
        } catch (Exception e) {
            error(e.toString());
            pdf = null;
        }
        return pdf;
    }

    /**
     * Log ERROR.
     *
     * @param error
     */
    private static void error(String error) {
        System.out.println("| ERROR: " + error);
    }

    public void relAlternativo(Emissao65 e) throws JRException, IOException {
        File someFile = new File("C:\\Users\\JW Junior\\Desktop\\xml\\" + e.getChave() + ".xml");
        InputStream xmlInputStream = FileUtils.openInputStream(someFile);

        String packageReport = "";
        String pathSubReport = URLDecoder.decode(getClass().getClassLoader().getResource(".").getPath() + packageReport, "UTF-8");

//        InputStream reportStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(report);
// essa linha converte o arquivo jasper em inputstrem        
        //InputStream reportStIream = getClass().getClassLoader().getResourceAsStream(packageReport + report);
        //m.put("XML_INPUT_STREAM", xmlInputStream);
        //m.put("SUBREPORT_DIR", pathSubReport);
        OutputStream out = new ByteArrayOutputStream();
        //JasperRunManager.runReportToPdfStream(reportStream, out, m);
        out.flush();
        out.close();
    }

    public void relatorioCadastralGrupo() {
        try {
            ReportsUtil util = new ReportsUtil();
            util.gerarRelatorioPDF(null, "WEB-INF/reports/grupo.jasper", getConnection());
        } catch (IOException | JRException | ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    public void relatorioaDanfe(Emissao65 e) {
        try {
            File someFile = new File(e.getCaminho());
            InputStream xmlInputStream = FileUtils.openInputStream(someFile);
            ReportsUtil util = new ReportsUtil();
            Map<String, Object> param = new HashMap<>();
            param.put("XML_INPUT_STREAM", xmlInputStream);
            util.gerarRelatorioPDF(param, "WEB-INF/reports/DanfeNFCe.jasper", getConnection());
        } catch (IOException | JRException | ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }
    public void relatorioaDanfe55(Emissao e) {
        try {
            File someFile = new File(e.getCaminho());
            InputStream xmlInputStream = FileUtils.openInputStream(someFile);
            ReportsUtil util = new ReportsUtil();
            Map<String, Object> param = new HashMap<>();
            param.put("XML_INPUT_STREAM", xmlInputStream);
            param.put("Logo", "WEB-INF/Logo/rja.png");
            util.gerarRelatorioPDF(param, "WEB-INF/reports/danfeR.jasper", getConnection());
        } catch (IOException | JRException | ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }
    public void relatorioaDanfe55P(Emissao e) {
        try {
            File someFile = new File(e.getCaminho());
            InputStream xmlInputStream = FileUtils.openInputStream(someFile);
            ReportsUtil util = new ReportsUtil();
            Map<String, Object> param = new HashMap<>();
            param.put("XML_INPUT_STREAM", xmlInputStream);
            param.put("Logo", "WEB-INF/Logo/rja.png");
            util.gerarRelatorioPDF(param, "WEB-INF/reports/danfeP.jasper", getConnection());
        } catch (IOException | JRException | ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    public Connection getConnection() throws SQLException {
        Session session = em.unwrap(Session.class);
        SessionFactoryImplementor sfi = (SessionFactoryImplementor) session.getSessionFactory();
        ConnectionProvider cp = sfi.getConnectionProvider();
        Connection connection = cp.getConnection();
        return connection;
    }

}
