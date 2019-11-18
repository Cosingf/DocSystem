package cn.xmu.edu.legaldocument.service.serviceImpl;

import org.pdfbox.exceptions.COSVisitorException;
import org.pdfbox.exceptions.InvalidPasswordException;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdfwriter.COSWriter;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.Splitter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Component
public class PDFSplit {
    private static final String PASSWORD = "-password";
    private static final String SPLIT = "-split";
    public  static int length;
    public  static String name[] =new String[100];

    public PDFSplit(){

    }

    public PDFSplit(int a[],MultipartFile file){
        PDFSplit split = new PDFSplit();
        try {
            split.split(a,file);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * Infamous main method.
     *
     * @param
     *
     * @throws Exception If there is an error parsing the document.
     */


    private void split(int a[], MultipartFile file) throws Exception
    {
        String password = "";
        String split = "1";
        String[] args={};
        Splitter splitter = new Splitter();
        String   myPath="F:/";
        String pdfFile = myPath+file.getOriginalFilename();
        for( int i=0; i<args.length; i++ )
        {
            if( args[i].equals( PASSWORD ) )
            {
                i++;
                if( i >= args.length )
                {
                    usage();
                }
                password = args[i];
            }
            else if( args[i].equals( SPLIT ) )
            {
                i++;
                if( i >= args.length )
                {
                    usage();
                }
                split = args[i];
            }
            else
            {
                if( pdfFile == null )
                {
                    pdfFile = args[i];
                }
            }
        }

        if( pdfFile == null )
        {
            usage();
        }
        else
        {

            InputStream input = null;
            PDDocument document = null;
            List documents = null;
            try
            {
                input = new FileInputStream( pdfFile );
                document = parseDocument( input );
                //文件解密
                if( document.isEncrypted() )
                {
                    try
                    {
                        document.decrypt( password );
                    }
                    catch( InvalidPasswordException e )
                    {
                        if( args.length == 4 )//they supplied the wrong password
                        {
                            System.err.println( "Error: The supplied password is incorrect." );
                            System.exit( 2 );
                        }
                        else
                        {
                            //they didn't suppply a password and the default of "" was wrong.
                            System.err.println( "Error: The document is encrypted." );
                            usage();
                        }
                    }
                }

                splitter.setSplitAtPage( Integer.parseInt( split ) );

                documents = splitter.split( document );
                //   for( int i=0; i<documents.size(); i++ )
                //  {

                for(int i=0; a[i]<documents.size()||a[i]==documents.size()&&documents.size()<a.length; i++){
                    PDDocument doc = (PDDocument)documents.get( a[i]-1);
                    //String fileName = pdfFile.substring(0, pdfFile.length()-4 ) + "-" + a[i] + ".pdf";
                    String fileName = myPath+a[i]+".pdf";
                    String txtfilePath = myPath+a[i]+".txt";
                    writeDocument(doc,fileName );
                    name[i]=fileName;
                    length++;
                    System.out.println("document:"+doc.getDocument());
                    System.out.println("a[i]"+a[i]);
                    doc.close();
                    //    }

                }
            }
            finally
            {
                if( input != null )
                {
                    input.close();
                }
                if( document != null )
                {
                    document.close();
                }
                for( int i=0; documents != null && i<documents.size(); i++ )
                {
                    PDDocument doc = (PDDocument)documents.get( i );
                    doc.close();
                }
            }
        }
    }



    private static final void writeDocument( PDDocument doc, String fileName ) throws IOException, COSVisitorException
    {
        FileOutputStream output = null;
        COSWriter writer = null;
        try
        {
            output = new FileOutputStream(fileName);
            writer = new COSWriter( output );
            writer.write( doc );
        }
        finally
        {
            if( output != null )
            {
                output.close();
            }
            if( writer != null )
            {
                writer.close();
            }
        }
    }

    /**
     * This will parse a document.
     *
     * @param input The input stream for the document.
     *
     * @return The document.
     *
     * @throws IOException If there is an error parsing the document.
     */
    private static PDDocument parseDocument( InputStream input )throws IOException
    {
        PDFParser parser = new PDFParser( input );
        parser.parse();
        return parser.getPDDocument();
    }

    /**
     * This will print the usage requirements and exit.
     */
    private static void usage()
    {
        System.err.println( "Usage: java org.pdfbox.PDFSplit [OPTIONS] <PDF file>\n" +
                "  -password  <password>        Password to decrypt document\n" +
                "  -split     <integer>         split after this many pages\n" +
                "  <PDF file>                   The PDF document to use\n"
        );
        System.exit( 1 );
    }
}
