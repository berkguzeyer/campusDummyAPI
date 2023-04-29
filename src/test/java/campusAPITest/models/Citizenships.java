package campusAPITest.models;

public class Citizenships {

    private String name;
    private String shortName;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Citizenships{" +
                "name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
