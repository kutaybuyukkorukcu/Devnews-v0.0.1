package model;

public class Like {

    private String title;
    private String mainTopic;
    private int isNew;

    public Like() {

    }

    public Like(String title, String mainTopic, int isNew) {
        this.title = title;
        this.mainTopic = mainTopic;
        this.isNew = isNew;
    }

    @Override
    public String toString() {
        return "Like{" +
                ", title='" + title + '\'' +
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

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }
}
