package crowd.entity;


public class Subject {
    private String subName;
    private Integer subScore;

    public Subject() {
    }

    public Subject(String subName, Integer subScore) {
        super();
        this.subName = subName;
        this.subScore = subScore;
    }

    public String getSubName() {
        return subName;
    }

    public Integer getSubScore() {
        return subScore;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public void setSubScore(Integer subScore) {
        this.subScore = subScore;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subName='" + subName + '\'' +
                ", subScore=" + subScore +
                '}';
    }
}
