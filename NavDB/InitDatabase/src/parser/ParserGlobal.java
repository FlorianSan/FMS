/*
 * Class to fill the SQL database
 */
package parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsapar.JSaParException;
import org.jsapar.input.Parser;
import org.jsapar.input.ParsingEventListener;
import org.jsapar.schema.Schema;
import org.jsapar.schema.SchemaException;
import org.jsapar.schema.Xml2SchemaBuilder;
import java.util.Properties;

/**
 * Class to fill the SQL database
 *
 * @author yoann
 *
 */
public class ParserGlobal {

   /**
    * ParserGlobal Constructor, parsed ARINC_0501_EUR.dat and fill SQL database 
    * @param con Database connection
    * @throws JSaParException 
    */
    public ParserGlobal(Connection con) throws JSaParException {

        // On ouvre le fichier xml general
        Reader fileSchemaGeneral = null;
        Reader fileReader = null;
        try {
            fileSchemaGeneral = new InputStreamReader(ParserGlobal.class.getResourceAsStream("parsergen.xml"), "UTF8");

            //XML lecteur
            Xml2SchemaBuilder builder = new Xml2SchemaBuilder();

            //creation des schémas
            Schema schemaGeneral = builder.build(fileSchemaGeneral);
            fileSchemaGeneral.close();

            //Données ARINC a lire
//            Reader fileReader = new FileReader("parser/data.txt");
            fileReader = new InputStreamReader(ParserGlobal.class.getResourceAsStream("Arinc_0501_EUR.dat"), "UTF8");;

            Parser parser = new Parser(schemaGeneral);


            ParsingEventListener a = new ListenerParser(con);

            parser.addParsingEventListener(a);

            //parsage du fichier
            parser.build(fileReader);

            //relier les holdings pattern a des balises
            HoldingPattern.updateHoldingPattern(con);

            //Remplissage de la table des SIDs
            Sid sid = new Sid();
            sid.creationSid(con);

            //Remplissage de la table des STARS
            Star star = new Star();
            star.creationStar(con);

            //Remplissage de la table APPR
            Approche appr = new Approche();
            appr.creationAppr(con);

        } catch (FileNotFoundException e) {
            System.out.println("ParserGlobal : fichier introuvable : " + e.getMessage());
        } catch (SchemaException e) {
            System.out.println("ParserGlobal : Probleme construction XML : " + e.getMessage());
        } catch (IOException ex) {
            System.out.println("ParserGlobal : probleme entree/sortie : " + ex.getMessage());
        } finally {
            if (fileSchemaGeneral != null) {
                try {
                    fileSchemaGeneral.close();
                } catch (IOException ex) {
                    Logger.getLogger(ParserGlobal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(ParserGlobal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Create SQL Connection, uses db.properties
     * @return Database Connection
     */
    public static Connection createSql() {
        InputStream in = null;

        try {
            Properties prop = new Properties();
            in = ParserGlobal.class.getResourceAsStream("db.properties");
            prop.load(in);
            in.close();
            // Extraction des propriétés
            String url = prop.getProperty("url");
            String user = prop.getProperty("user");
            String password = prop.getProperty("password");
            return DriverManager.getConnection(url, user, password);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParserGlobal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ParserGlobal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ParserGlobal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(ParserGlobal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return null;
    }

   /**
    * Main Method to parsed and fill the SQL database
    * @param args Commande arguments
    * @throws SchemaException
    * @throws JSaParException 
    */
    public static void main(String[] args) throws SchemaException, JSaParException {
        Connection con = null;
        try {
            con = ParserGlobal.createSql();
            ParserGlobal parserGlobal = new ParserGlobal(con);
        }
        finally{
            if(con != null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ParserGlobal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
}
