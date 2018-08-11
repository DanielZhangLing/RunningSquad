package neu.edu.runningsquad.model;

import java.sql.Time;

public class Record {
    String challenge;
    int receivedPrize;
    Long dateTime;

    public Record(){

    }

    public Record(String challenge, int receivedPrize, Long dateTime) {
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

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }
}
