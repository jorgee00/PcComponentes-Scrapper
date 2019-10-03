package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class main {
    public static final String url = "https://www.pccomponentes.com/portatiles/pc-para-gaming/ultrabook/workstation";


    public static void main (String args[]) {
        int num = 0;
        try {
            PrintWriter writer = new PrintWriter("db.csv","UTF-8");
            int i = 1;
            while (true){

                String urlPage = url + "?page=" + i;
                System.out.println("Comprobando entradas de: "+urlPage);

                // Compruebo si me da un 200 al hacer la petición
                if (getStatusConnectionCode(urlPage) == 200) {

                    // Obtengo el HTML de la web en un objeto Document2
                    Document document = getHtmlDocument(urlPage);

                    // Busco todas las historias de meneame que estan dentro de:
                    Elements entradas = document.select("div.col-xs-6.col-sm-4.col-md-4.col-lg-4");
                    for (Element elem : entradas) {
                        Element value = elem.select("a").first();
                        String name = value.attr("data-name");
                        if(!name.contains("Reacondicionado")) {
                            name = name.replace("\"","'");
                            String brand = value.attr("data-brand");
                            String price = value.attr("data-price");
                            String href = value.attr("href");
                            writer.println("\"" + name + "\",\"" + brand + "\",\"" + price + "€\",\"" + "https://www.pccomponentes.com" + href + "\"");
                            System.out.println("\"" + name + "\",\"" + brand + "\",\"" + price + "\",\"" + "https://www.pccomponentes.com" +href +"€\"" );
                            num += 1;
                        }
                    }
                    i++;
                }else {
                    System.out.println("El Status Code no es OK es: " + getStatusConnectionCode(urlPage));
                    break;
                }
            }
            writer.close();
            System.out.println(num);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    /**
     * Devuelve el codigo de la pagina correspondiente al estado de la pagna
     *
     *@param url
     *@return codigo de respuesta
     */
    public static int getStatusConnectionCode(String url) {

        Response response = null;

        try {
            return Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute().statusCode();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
            return 404;
        }
    }


    /**
     * Devuelve el html de la pagina correspondente al enlace que pasamos
     * @param url
     * @return Documento con el HTML
     */
    public static Document getHtmlDocument(String url) {
        try {
            return Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
            return null;
        }
    }
}
