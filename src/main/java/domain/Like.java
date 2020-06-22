package domain;

public class Like {

    private String title;
    private String mainTopic;
    private boolean isNew;

    public Like() {

    }

    public Like(String title, String mainTopic, boolean isNew) {
        this.title = title;
        this.mainTopic = mainTopic;
        this.isNew = isNew;
    }

    @Override
    public String toString() {
        return "Like{" +
                "title='" + title + '\'' +
                ", mainTopic='" + mainTopic + '\'' +
                ", isNew=" + isNew +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainTopic() {
        return mainTopic;
    }

    public void setMainTopic(String mainTopic) {
        this.mainTopic = mainTopic;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
}
