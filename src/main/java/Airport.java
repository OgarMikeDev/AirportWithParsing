import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
                            for (Element elementForDepartureFlight : elementsForFlights) {
                                String strElementForDepartureFlight = elementForDepartureFlight.toString();

                                Flight.TypeFlight typeFlight = Flight.TypeFlight.DEPARTURE;

                                String templateForNameAirline = "class=\"fade-string\">";
                                int startIndexForNameAirline = strElementForDepartureFlight.indexOf(templateForNameAirline);
                                if (startIndexForNameAirline == -1) {
                                    continue;
                                }
                                startIndexForNameAirline += templateForNameAirline.length();
                                int endIndexForNameAirline = strElementForDepartureFlight.indexOf("</span>", startIndexForNameAirline);
                                String nameAirline = strElementForDepartureFlight.substring(startIndexForNameAirline, endIndexForNameAirline);

                                String templateForNumberFlight = nameAirline + "</span>\s";
                                int startIndexForNumberFlight = strElementForDepartureFlight.indexOf(templateForNumberFlight);
                                if (startIndexForNumberFlight == -1) {
                                    continue;
                                }
                                startIndexForNumberFlight += templateForNumberFlight.length();
                                int endIndexForNumberFlight = strElementForDepartureFlight.indexOf("</td>", startIndexForNumberFlight);
                                String numberFlight = strElementForDepartureFlight.substring(startIndexForNumberFlight, endIndexForNumberFlight);

                                String templateForPlaceForArrival = numberFlight + "</td>\n\s<td>";
                                int startIndexForPlaceForArrival = strElementForDepartureFlight.indexOf(templateForPlaceForArrival);
                                if (startIndexForPlaceForArrival == -1) {
                                    continue;
                                }
                                startIndexForPlaceForArrival += templateForPlaceForArrival.length();
                                int endIndexForPlaceForArrival = strElementForDepartureFlight.indexOf("</td>", startIndexForPlaceForArrival);
                                String placeForArrival = strElementForDepartureFlight.substring(
                                        startIndexForPlaceForArrival, endIndexForPlaceForArrival);

                                String templateForTimeDeparture = placeForArrival + "</td>\n\s<td>";
                                int startForTimeDeparture = strElementForDepartureFlight.indexOf(templateForTimeDeparture);
                                if (startForTimeDeparture == -1) {
                                    continue;
                                }
                                startForTimeDeparture += templateForTimeDeparture.length();
                                int endForTimeDeparture = strElementForDepartureFlight.indexOf("</td>", startForTimeDeparture);
                                String strTimeDeparture = strElementForDepartureFlight.substring(startForTimeDeparture, endForTimeDeparture);
                                String[] arrayStrHoursAndMinutes = strTimeDeparture.split(":");
                                int hours = Integer.parseInt(arrayStrHoursAndMinutes[0]);
                                int minutes = Integer.parseInt(arrayStrHoursAndMinutes[1]);
                                LocalDateTime timeDeparture = LocalDateTime.of(
                                        LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(),
                                        hours, minutes
                                );

                                String templateForDurationFlight = strTimeDeparture + "</td>\n\s<td>";
                                int startIndexForDurationFlight = strElementForDepartureFlight.indexOf(templateForDurationFlight);
                                if (startIndexForDurationFlight == -1) {
                                    continue;
                                }
                                startIndexForDurationFlight += templateForDurationFlight.length();
                                int endIndexForDurationFlight = strElementForDepartureFlight.indexOf("</td>", startIndexForDurationFlight);
                                String durationFlight = strElementForDepartureFlight.substring(startIndexForDurationFlight, endIndexForDurationFlight);

                                String templateForTimeArrival = durationFlight + "</td>\n\s<td>";
                                int startForTimeArrival = strElementForDepartureFlight.indexOf(templateForTimeArrival);
                                if (startForTimeArrival == -1) {
                                    continue;
                                }
                                startForTimeArrival += templateForTimeArrival.length();
                                int endForTimeArrival = strElementForDepartureFlight.indexOf("</td>", startForTimeArrival);
                                String strTimeArrival = strElementForDepartureFlight.substring(startForTimeArrival, endForTimeArrival);
                                String[] arrayStrHoursAndMinutesArrival = strTimeArrival.split(":");
                                int hoursForArrival = Integer.parseInt(arrayStrHoursAndMinutesArrival[0]);
                                int minutesForArrival = Integer.parseInt(arrayStrHoursAndMinutesArrival[1]);
                                LocalDateTime timeArrival = LocalDateTime.of(
                                        LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(),
                                        hoursForArrival, minutesForArrival
                                );

                                System.out.println(
                                        "\uD83C\uDF89" +
                                        typeFlight + "\n" +
                                        nameAirline + "\n" +
                                        numberFlight + "\n" +
                                        placeForArrival + "\n" +
                                        timeDeparture + "\n" +
                                        durationFlight + "\n" +
                                        timeArrival +
                                        "\uD83C\uDF89");
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
