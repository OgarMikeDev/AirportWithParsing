import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Airport {
    private final String urlMainPageAviaSales = "https://www.aviasales.ru";
    private Map<String, String> mapAllAirports;
    private final String pathToFilesHtml = "src/main/resources/data/";

    public Airport() {
        mapAllAirports = new TreeMap<>();
        fillMapAllAirports();
    }

    public Document returnHtml(String urlPage) {
        Document document = null;

        try {
            document = Jsoup.connect(urlPage).get();
        } catch (Exception ex) {
            ex.getMessage();
        }

        return document;
    }

    //TODO Наполнение map всех аэропортов
    public void fillMapAllAirports() {
        try {
            Document documentMainPageAviaSales = returnHtml(urlMainPageAviaSales);
            String templateForLinkAllAirports = "href=\"/airports\"";
            int startIndexForLinkAllAirports = documentMainPageAviaSales.toString()
                    .indexOf(templateForLinkAllAirports);
            if (startIndexForLinkAllAirports == -1) {
                return;
            }
            startIndexForLinkAllAirports += 6;
            int endIndexForLinkAllAirports = documentMainPageAviaSales.toString()
                    .indexOf("\"", startIndexForLinkAllAirports);
            String linkAllAirports = urlMainPageAviaSales +
                    documentMainPageAviaSales.toString()
                            .substring(startIndexForLinkAllAirports, endIndexForLinkAllAirports);

            Document documentAllAirports = returnHtml(linkAllAirports);
            FileWriter fileWriter = new FileWriter(pathToFilesHtml + "Аэропорты.html");
            fileWriter.write(documentAllAirports.toString());

            Elements elementsAllAirports = documentAllAirports.select(
                    ".index-list__item.is-active");
            for (Element elementAirport : elementsAllAirports) {
                String strElementAirport = elementAirport.toString();

                String templateForNameAirport = "data-original-name=\"";
                int startIndexForNameAirport = strElementAirport.indexOf(templateForNameAirport);
                if (startIndexForNameAirport == -1) {
                    continue;
                }
                startIndexForNameAirport += templateForNameAirport.length();
                int endIndexForNameAirport = strElementAirport.indexOf("\"", startIndexForNameAirport);
                String nameAirport = strElementAirport.substring(startIndexForNameAirport, endIndexForNameAirport);

                String templateForLink = "href=\"/airports";
                int startIndexForLink = strElementAirport.indexOf(templateForLink);
                if (startIndexForLink == -1) {
                    continue;
                }
                startIndexForLink += templateForLink.length();
                int endIndexForLink = strElementAirport.indexOf("\"", startIndexForLink);
                String linkAirport = linkAllAirports +
                        strElementAirport.substring(startIndexForLink, endIndexForLink);

                mapAllAirports.put(nameAirport, linkAirport);
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    //TODO Вывод всех аэропортов в удобочитаемом формате
    public void printMapAllAirports() {
        for (Map.Entry<String, String> entryAirport : mapAllAirports.entrySet()) {
            System.out.println("\uD83C\uDF89" + entryAirport.getKey() + "\n" + entryAirport.getValue() + "\uD83C\uDF89");
        }
    }

    /*
        TODO
         Ввод названия аэропорта
         и получение списка
         всех вылетов из него
    */
    public List<Flight> getListAllDepartureFlightsFromSelectedUserAirport(String nameAirport) {
        List<Flight> listAllDepartureFlightsFromSelectedUserAirport = new ArrayList<>();
        for (Map.Entry<String, String> entryAirport : mapAllAirports.entrySet()) {
            if (entryAirport.getKey().compareToIgnoreCase(nameAirport) == 0) {
                Document documentForAirport = returnHtml(entryAirport.getValue());
                try {
                    FileWriter fileWriter = new FileWriter(pathToFilesHtml + nameAirport + ".html");
                    fileWriter.write(documentForAirport.toString());

                    Elements elementsForAirportsAndFlights = documentForAirport.select(".page__part");
                    for (Element elementForAirportOrFlight : elementsForAirportsAndFlights) {
                        String strElementForAirportOrFlight = elementForAirportOrFlight.toString();
                        if (strElementForAirportOrFlight.contains("из")) {
                            Elements elementsForFlights = elementForAirportOrFlight.select(".hidden-link");
                            for (Element elementForFlight : elementsForFlights) {
                                String strElementForFlight = elementForFlight.toString();
                                String templateForNameAirline = "class=\"fade-string\">";
                                int startIndexForNameAirline = strElementForFlight.indexOf(templateForNameAirline);
                                if (startIndexForNameAirline == -1) {
                                    continue;
                                }
                                startIndexForNameAirline += templateForNameAirline.length();
                                int endIndexForNameAirline = strElementForFlight.indexOf("</span>", startIndexForNameAirline);
                                String nameAirline = strElementForFlight.substring(startIndexForNameAirline, endIndexForNameAirline);

                                System.out.println("\uD83C\uDF89" + nameAirline + "\uD83C\uDF89");
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        }
        return listAllDepartureFlightsFromSelectedUserAirport;
    }
}
