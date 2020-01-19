package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Validator {

    public Map<String, String> generic = new HashMap();

    public Validator() {
        setDevelopment();
        setArchitecture();
        setAI();
        setCulture();
        setDevops();
    }

    public String validate(String topic) {
        return generic.get(topic);
    }

    public String removeLastChar(String s) {
        return Optional.ofNullable(s)
                .filter(str -> str.length() != 0)
                .map(str -> str.substring(0, str.length() - 1))
                .orElse(s);
    }

    public void setDevelopment() {
        generic.put("Development", "Development");
        generic.put("Java", "Development");
        generic.put("Kotlin", "Development");
        generic.put(".NET", "Development");
        generic.put("C#", "Development");
        generic.put("Swift", "Development");
        generic.put("Go", "Development");
        generic.put("Rust", "Development");
        generic.put("JavaScript", "Development");
        generic.put("Mobile", "Development");
        generic.put("Web Development", "Development");
        generic.put("Emerging Technologies", "Development");
    }

    public void setArchitecture() {
        generic.put("Architecture & Design", "Architecture & Design");
        generic.put("Architecture", "Architecture & Design");
        generic.put("Enterprise Architecture", "Architecture & Design");
        generic.put("Scalability/Performance", "Architecture & Design");
        generic.put("Design", "Architecture & Design");
        generic.put("Case Studies", "Architecture & Design");
        generic.put("Microservices", "Architecture & Design");
        generic.put("Patterns", "Architecture & Design");
        generic.put("Security", "Architecture & Design");
    }

    public void setAI() {
        generic.put("AI, ML & Data Engineering", "AI, ML & Data Engineering");
        generic.put("Big Data", "AI, ML & Data Engineering");
        generic.put("Machine Learning", "AI, ML & Data Engineering");
        generic.put("NoSQL", "AI, ML & Data Engineering");
        generic.put("Database", "AI, ML & Data Engineering");
        generic.put("Data Analytics", "AI, ML & Data Engineering");
        generic.put("Streaming", "AI, ML & Data Engineering");
    }

    public void setCulture() {
        generic.put("Culture & Methods", "Culture & Methods");
        generic.put("Agile", "Culture & Methods");
        generic.put("Diversity", "Culture & Methods");
        generic.put("Leadership", "Culture & Methods");
        generic.put("Lean/Kanban", "Culture & Methods");
        generic.put("Personal Growth", "Culture & Methods");
        generic.put("Scrum", "Culture & Methods");
        generic.put("Sociocracy", "Culture & Methods");
        generic.put("Software Craftmanship", "Culture & Methods");
        generic.put("Team Collaboration", "Culture & Methods");
        generic.put("Testing", "Culture & Methods");
        generic.put("UX", "Culture & Methods");
    }

    public void setDevops() {
        generic.put("DevOps", "DevOps");
        generic.put("Infrastructure", "DevOps");
        generic.put("Continuous Delivery", "DevOps");
        generic.put("Automation", "DevOps");
        generic.put("Containers", "DevOps");
        generic.put("Cloud", "DevOps");
        generic.put("Observability", "DevOps");
    }
}
