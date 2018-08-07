package neu.edu.runningsquad.model;

public class Challenge {
    String name;
    String content;
    String startPosition;
    String endPosition;
    String prize;

    public Challenge(String name, String content, String startPosition, String endPosition, String prize) {
        this.name = name;
        this.content = content;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.prize = prize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    public String getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }
}
