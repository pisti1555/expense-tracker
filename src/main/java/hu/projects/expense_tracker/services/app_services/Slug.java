package hu.projects.expense_tracker.services.app_services;

public class Slug {
    public static String of(String str) {
        return str
                .toLowerCase()
                .replace(" ", "_")
                .replace("&", "and")
                .replaceAll("[^a-z_]", "");
    }

    public static boolean isSlugFormat(String str) {
        return str.matches("[a-z_]+");
    }
}
