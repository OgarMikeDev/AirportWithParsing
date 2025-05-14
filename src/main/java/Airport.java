import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class Airport {
    private String pathToHtmlCodeMainPageAirport = "src/main/resources/data/";
    private Map<String, String> mapAirports;

    public Airport(String urlMainPageAirports) {
        mapAirports = new HashMap<>();
        saveMapAirports(urlMainPageAirports);
    }

    public Document parseHtmlCode(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return document;
    }

    public void saveMapAirports(String urlMainPageAirports) {
        try {
            Document document = Jsoup.connect(urlMainPageAirports).get();
            Elements elements = document.select(".s__yB3EapYI1kWLVKzMbxuf");
            String strDocument = document.toString();

            FileWriter fileWriter = new FileWriter(pathToHtmlCodeMainPageAirport + "mainPageAirports.html");
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

    //TODO вывод всех аэропортов с ссылками на их страницами
    public void printMapAirports() {
        for (Map.Entry<String, String> entryForAirport : mapAirports.entrySet()) {
            System.out.println("\uD83C\uDCCF" + entryForAirport.getKey() + "\n" + entryForAirport.getValue() + "\uD83C\uDCCF");
        }
    }

    //TODO вывод списка вылетов из выбранного пользователем аэропорта
    public void printListDepartureFlightsFromSelectedUserAirport(String nameAirport) {
        for (Map.Entry<String, String> entryForAirport : mapAirports.entrySet()) {
            if (entryForAirport.getKey().compareToIgnoreCase(nameAirport) == 0) {
                Document document = parseHtmlCode(entryForAirport.getValue());
                try {
                    FileWriter fileWriter = new FileWriter(pathToHtmlCodeMainPageAirport + "/" + nameAirport + ".html");
                    fileWriter.write(document.toString());

                    Elements elements = document.select(".hidden-link");
                    for (Element element : elements) {
                        String strElement = element.toString();
                        //System.out.println("\uD83C\uDCCF" + strElement + "\uD83C\uDCCF");

                        String templateForNameAirline = "class=\"fade-string\">";
                        int startIndexForNameAirline = strElement.indexOf(templateForNameAirline);
                        if (startIndexForNameAirline == -1) {
                            continue;
                        }
                        startIndexForNameAirline += templateForNameAirline.length();
                        int endIndexForNameAirline = strElement.indexOf("</span>", startIndexForNameAirline);
                        String nameAirline = strElement.substring(startIndexForNameAirline, endIndexForNameAirline);

                        String templateForNumberFlight = "</span> ";
                        int startIndexForNumberFlight = strElement.indexOf(templateForNumberFlight);
                        if (startIndexForNumberFlight == -1) {
                            continue;
                        }
                        startIndexForNumberFlight += templateForNumberFlight.length();
                        int endIndexForNumberFlight = strElement.indexOf("</td>", startIndexForNumberFlight);
                        String numberFlight = strElement.substring(startIndexForNumberFlight, endIndexForNumberFlight);

                        String templateForPlaceForArrival = numberFlight + "</td>\n\s<td>";
                        int startIndexForPlaceForArrival = strElement.indexOf(templateForPlaceForArrival);
                        if (startIndexForPlaceForArrival == -1) {
                            continue;
                        }
                        startIndexForPlaceForArrival += templateForPlaceForArrival.length();
                        int endIndexForPlaceForArrival = strElement.indexOf("</td>", startIndexForPlaceForArrival);
                        String placeForArrival = strElement.substring(startIndexForPlaceForArrival, endIndexForPlaceForArrival);

                        String templateForTimeDeparture = placeForArrival + "</td>\n <td>";
                        int startIndexForTimeDeparture = strElement.indexOf(templateForTimeDeparture);
                        if (startIndexForTimeDeparture == -1) {
                            continue;
                        }
                        startIndexForTimeDeparture += templateForTimeDeparture.length();
                        int endIndexForTimeDeparture = strElement.indexOf("</td>", startIndexForTimeDeparture);
                        String timeDeparture = strElement.substring(startIndexForTimeDeparture, endIndexForTimeDeparture);

                        System.out.println("\uD83C\uDCCF" + nameAirline + "\n" +
                                numberFlight.replaceAll("\\(|\\)", "") + "\n" +
                                placeForArrival + "\n" +
                                timeDeparture + "\uD83C\uDCCF");
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        }
    }
}
