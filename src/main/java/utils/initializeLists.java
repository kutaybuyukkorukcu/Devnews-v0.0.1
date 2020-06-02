package utils;

import model.Article;
import model.Data;

import java.util.ArrayList;

public class initializeLists {

    public static ArrayList<Article> development = null;
    public static ArrayList<Article> architecture = null;
    public static ArrayList<Article> ai = null;
    public static ArrayList<Article> culture = null;
    public static ArrayList<Article> devops = null;
    public static ArrayList<Data> recommendedArticles = null;

    public static void generateLists() {
        development = new ArrayList<>();
        architecture = new ArrayList<>();
        ai = new ArrayList<>();
        culture = new ArrayList<>();
        devops = new ArrayList<>();
        recommendedArticles = new ArrayList<>();
    }

}
