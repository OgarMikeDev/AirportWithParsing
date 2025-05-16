public class Flight {
    private TypeFlight typeFlight;
    private String nameAirline;
    private String numberFlight;
    private String placeForArrival;
    private String timeDeparture;
    private String timeFlight;
    private String timeArrival;
    private String daysForDeparture;

    public Flight(
            TypeFlight typeFlight, String nameAirline,
            String numberFlight, String placeForArrival,
            String timeDeparture, String timeFlight,
            String timeArrival, String daysForDeparture
    ) {
        this.nameAirline = nameAirline;
        this.numberFlight = numberFlight;
        this.placeForArrival = placeForArrival;
        this.timeDeparture = timeDeparture;
        this.timeFlight = timeFlight;
        this.daysForDeparture = daysForDeparture;
        this.timeArrival = timeArrival;
        this.typeFlight = typeFlight;
    }

    public TypeFlight getTypeFlight() {
        return typeFlight;
    }

    public String getNameAirline() {
        return nameAirline;
    }

    public String getNumberFlight() {
        return numberFlight;
    }

    public String getPlaceForArrival() {
        return placeForArrival;
    }

    public String getTimeDeparture() {
        return timeDeparture;
    }

    public String getTimeFlight() {
        return timeFlight;
    }

    public String getTimeArrival() {
        return timeArrival;
    }

    public String getDaysForDeparture() {
        return daysForDeparture;
    }

    @Override
    public String toString() {
        return "\uD83C\uDCCF\nFlight{" +
                "typeFlight=" + typeFlight +
                ", nameAirline='" + nameAirline + '\'' +
                ", numberFlight='" + numberFlight + '\'' +
                ", placeForArrival='" + placeForArrival + '\'' +
                ", timeDeparture='" + timeDeparture + '\'' +
                ", timeFlight='" + timeFlight + '\'' +
                ", timeArrival='" + timeArrival + '\'' +
                ", daysForDeparture='" + daysForDeparture + '\'' +
                '}' + "\n\uD83C\uDCCF";
    }

    enum TypeFlight {
        DEPARTURE,
        ARRIVAL
    }
}
