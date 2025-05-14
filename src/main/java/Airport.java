import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class Airport {
    private String pathToHtmlCodeMainPageAirport = "src/main/resources/data/mainPageAirports.html";
    private Map<String, String> mapAirports;

    public Airport(String urlMainPageAirports) {
        mapAirports = new HashMap<>();
        parseMainPageAirports(urlMainPageAirports);
    }

    public void parseMainPageAirports(String urlMainPageAirports) {
        try {
            Document document = Jsoup.connect(urlMainPageAirports).get();
            Elements elements = document.select(".s__yB3EapYI1kWLVKzMbxuf");
            String strDocument = document.toString();

            FileWriter fileWriter = new FileWriter(pathToHtmlCodeMainPageAirport);
            fileWriter.write(strDocument);

            for (Element element : elements) {
                String strElement = element.toString();
                for (String line : strElement.split("</a>")) {
                    if (strElement.contains("href=\"/airports\"")) {
                        String templateForLinkAirport = "href=\"";
                        int startIndexForLinkAirport = line.indexOf(templateForLinkAirport);
                        if (startIndexForLinkAirport == -1) {
                            continue;
                        }
                        startIndexForLinkAirport += templateForLinkAirport.length();
                        int endIndexForLinkAirport = line.indexOf("\"", startIndexForLinkAirport);
                        String linkAirport = urlMainPageAirports + line.substring(startIndexForLinkAirport, endIndexForLinkAirport);

                        String templateForNameAirport = "data-test-id=\"text\">";
                        int startIndexForNameAirport = line.indexOf(templateForNameAirport);
                        if (startIndexForNameAirport == -1) {
                            continue;
                        }
                        startIndexForNameAirport += templateForNameAirport.length();
                        String nameAirport = line.substring(startIndexForNameAirport);

                        if (nameAirport.equals("Аэропорты")) {
                            continue;
                        }
                        mapAirports.put(nameAirport, linkAirport);

                    }
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public Map<String, String> getMapAirports() {
        return mapAirports;
    }
}
