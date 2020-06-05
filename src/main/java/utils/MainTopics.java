package utils;

public enum MainTopics {
    DEVELOPMENT("Development"),
    ARCHITECTURE("Architecture & Design"),
    AI("AI, ML & Data Engineering"),
    CULTURE("Culture & Methods"),
    DEVOPS("DevOps");

    private String mainTopic;

    MainTopics(String mainTopic) {
        this.mainTopic = mainTopic;
    }

    public String getMainTopic() {
        return mainTopic;
    }
}