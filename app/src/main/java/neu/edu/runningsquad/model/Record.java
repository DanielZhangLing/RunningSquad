package neu.edu.runningsquad.model;

import java.sql.Time;

public class Record {
    String challenge;
    int receivedPrize;
    Time dateTime;


    public Record(String challenge, int receivedPrize, Time dateTime) {
        this.challenge = challenge;
        this.receivedPrize = receivedPrize;
        this.dateTime = dateTime;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public int getReceivedPrize() {
        return receivedPrize;
    }

    public void setReceivedPrize(int receivedPrize) {
        this.receivedPrize = receivedPrize;
    }

    public Time getDateTime() {
        return dateTime;
    }

    public void setDateTime(Time dateTime) {
        this.dateTime = dateTime;
    }
}
