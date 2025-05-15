import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    //TODO возвращение списка вылетов из выбранного пользователем аэропорта
    public List<Flight> getListDepartureFlightsFromSelectedUserAirport(String nameAirport) {
        List<Flight> listDepartureFlightSelectedAirport = new ArrayList<>();
        for (Map.Entry<String, String> entryForAirport : mapAirports.entrySet()) {
            if (entryForAirport.getKey().compareToIgnoreCase(nameAirport) == 0) {
                Document document = parseHtmlCode(entryForAirport.getValue());
                try {
                    FileWriter fileWriter = new FileWriter(pathToHtmlCodeMainPageAirport + "/" + nameAirport + ".html");
                    fileWriter.write(document.toString());

                    Elements elementsDirection = document.select(".page__part");
                    for (Element elementDirection : elementsDirection) {
                        String strElementDirection = elementDirection.toString();
                        if (strElementDirection.contains("<strong>Прямые рейсы из")) {
                            Elements elements = elementDirection.select(".hidden-link");
                            for (Element element : elements) {
                                String strElement = element.toString();

                                Flight.TypeFlight typeFlight = Flight.TypeFlight.DEPARTURE;

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

                                String templateForTimeDeparture = placeForArrival + "</td>\n\s<td>";
                                int startIndexForTimeDeparture = strElement.indexOf(templateForTimeDeparture);
                                if (startIndexForTimeDeparture == -1) {
                                    continue;
                                }
                                startIndexForTimeDeparture += templateForTimeDeparture.length();
                                int endIndexForTimeDeparture = strElement.indexOf("</td>", startIndexForTimeDeparture);
                                String timeDeparture = strElement.substring(startIndexForTimeDeparture, endIndexForTimeDeparture);

                                String templateForTimeFlight = timeDeparture + "</td>\n\s<td>";
                                int startIndexForTimeFlight = strElement.indexOf(templateForTimeFlight);
                                if (startIndexForTimeFlight == -1) {
                                    continue;
                                }
                                startIndexForTimeFlight += templateForTimeFlight.length();
                                int endIndexForTimeFlight = strElement.indexOf("</td>", startIndexForTimeFlight);
                                String timeFlight = strElement.substring(startIndexForTimeFlight, endIndexForTimeFlight);

                                String templateForTimeArrival = timeFlight + "</td>\n\s<td>";
                                int startIndexForTimeArrival = strElement.indexOf(templateForTimeArrival);
                                if (startIndexForTimeArrival == -1) {
                                    continue;
                                }
                                startIndexForTimeArrival += templateForTimeArrival.length();
                                int endIndexForTimeArrival = strElement.indexOf("</td>", startIndexForTimeArrival);
                                String timeArrival = strElement.substring(startIndexForTimeArrival, endIndexForTimeArrival);

                                String templateForDaysForDeparture = "class=\"hidden-link__replacement\">";
                                int startIndexForDaysForDeparture = strElement.indexOf(templateForDaysForDeparture);
                                if (startIndexForDaysForDeparture == -1) {
                                    continue;
                                }
                                startIndexForDaysForDeparture += templateForDaysForDeparture.length();
                                int endIndexForDaysForDeparture = strElement.indexOf("</div>", startIndexForDaysForDeparture);
                                String daysForDeparture = strElement.substring(startIndexForDaysForDeparture, endIndexForDaysForDeparture);

                                Flight flight = new Flight(
                                        typeFlight, nameAirline,
                                        numberFlight, placeForArrival,
                                        timeDeparture, timeFlight,
                                        timeArrival, daysForDeparture);

                                listDepartureFlightSelectedAirport.add(flight);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        }
        return listDepartureFlightSelectedAirport;
    }

    //TODO вывод списка прилётов в выбранный пользователем аэропорт
    public List<Flight> getListArrivalFlightsFromSelectedUserAirport(String nameAirport) {
        List<Flight> listDepartureFlightSelectedAirport = new ArrayList<>();
        for (Map.Entry<String, String> entryForAirport : mapAirports.entrySet()) {
            if (entryForAirport.getKey().compareToIgnoreCase(nameAirport) == 0) {
                Document document = parseHtmlCode(entryForAirport.getValue());
                try {
                    FileWriter fileWriter = new FileWriter(pathToHtmlCodeMainPageAirport + "/" + nameAirport + ".html");
                    fileWriter.write(document.toString());

                    Elements elementsDirection = document.select(".page__part");
                    for (Element elementDirection : elementsDirection) {
                        String strElementDirection = elementDirection.toString();
                        if (strElementDirection.contains("<strong>Прямые рейсы в")) {
                            Elements elements = elementDirection.select(".hidden-link");
                            for (Element element : elements) {
                                String strElement = element.toString();

                                Flight.TypeFlight typeFlight = Flight.TypeFlight.ARRIVAL;

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

                                String templateForTimeDeparture = placeForArrival + "</td>\n\s<td>";
                                int startIndexForTimeDeparture = strElement.indexOf(templateForTimeDeparture);
                                if (startIndexForTimeDeparture == -1) {
                                    continue;
                                }
                                startIndexForTimeDeparture += templateForTimeDeparture.length();
                                int endIndexForTimeDeparture = strElement.indexOf("</td>", startIndexForTimeDeparture);
                                String timeDeparture = strElement.substring(startIndexForTimeDeparture, endIndexForTimeDeparture);

                                String templateForTimeFlight = timeDeparture + "</td>\n\s<td>";
                                int startIndexForTimeFlight = strElement.indexOf(templateForTimeFlight);
                                if (startIndexForTimeFlight == -1) {
                                    continue;
                                }
                                startIndexForTimeFlight += templateForTimeFlight.length();
                                int endIndexForTimeFlight = strElement.indexOf("</td>", startIndexForTimeFlight);
                                String timeFlight = strElement.substring(startIndexForTimeFlight, endIndexForTimeFlight);

                                String templateForTimeArrival = timeFlight + "</td>\n\s<td>";
                                int startIndexForTimeArrival = strElement.indexOf(templateForTimeArrival);
                                if (startIndexForTimeArrival == -1) {
                                    continue;
                                }
                                startIndexForTimeArrival += templateForTimeArrival.length();
                                int endIndexForTimeArrival = strElement.indexOf("</td>", startIndexForTimeArrival);
                                String timeArrival = strElement.substring(startIndexForTimeArrival, endIndexForTimeArrival);

                                String templateForDaysForDeparture = "class=\"hidden-link__replacement\">";
                                int startIndexForDaysForDeparture = strElement.indexOf(templateForDaysForDeparture);
                                if (startIndexForDaysForDeparture == -1) {
                                    continue;
                                }
                                startIndexForDaysForDeparture += templateForDaysForDeparture.length();
                                int endIndexForDaysForDeparture = strElement.indexOf("</div>", startIndexForDaysForDeparture);
                                String daysForDeparture = strElement.substring(startIndexForDaysForDeparture, endIndexForDaysForDeparture);

                                Flight flight = new Flight(
                                        typeFlight, nameAirline,
                                        numberFlight, placeForArrival,
                                        timeDeparture, timeFlight,
                                        timeArrival, daysForDeparture);

                                listDepartureFlightSelectedAirport.add(flight);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        }
        return listDepartureFlightSelectedAirport;
    }

    //TODO возвращение ближайшего прилёта в выбранный пользователем аэропорт
    public Flight printFirstFlightArrivalInSelectedUserAirport(String nameAirport) {
        for (Flight flight : getListArrivalFlightsFromSelectedUserAirport(nameAirport)) {

        }
        return null;
    }
}
