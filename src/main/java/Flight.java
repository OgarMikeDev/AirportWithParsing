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

    public void setTypeFlight(TypeFlight typeFlight) {
        this.typeFlight = typeFlight;
    }

    public void setNameAirline(String nameAirline) {
        this.nameAirline = nameAirline;
    }

    public void setNumberFlight(String numberFlight) {
        this.numberFlight = numberFlight;
    }

    public void setPlaceForArrival(String placeForArrival) {
        this.placeForArrival = placeForArrival;
    }

    public void setTimeDeparture(String timeDeparture) {
        this.timeDeparture = timeDeparture;
    }

    public void setTimeFlight(String timeFlight) {
        this.timeFlight = timeFlight;
    }

    public void setTimeArrival(String timeArrival) {
        this.timeArrival = timeArrival;
    }

    public void setDaysForDeparture(String daysForDeparture) {
        this.daysForDeparture = daysForDeparture;
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
