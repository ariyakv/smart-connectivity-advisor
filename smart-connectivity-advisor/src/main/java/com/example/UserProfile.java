package com.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UserProfile {
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UserProfile {

    static class InputData {
        double monthlyBudget;
        int householdSize;
        int connectedDevices;
        boolean school;
        boolean remoteWork;
        boolean streaming;
        boolean gaming;
        boolean browsing;
        boolean reliabilityImportant;
        boolean slowSpeeds;
        boolean outages;
    }

    static class Plan {
        String id;
        String name;
        int speedMbps;
        double price;
        int reliabilityTier;
        String bestFor;

        Plan(String id, String name, int speedMbps, double price, int reliabilityTier, String bestFor) {
            this.id = id;
            this.name = name;
            this.speedMbps = speedMbps;
            this.price = price;
            this.reliabilityTier = reliabilityTier;
            this.bestFor = bestFor;
        }
    }

    static class RecommendationResult {
        Plan recommendedPlan;
        String backupRecommendation;
        int estimatedSpeedNeedMbps;
        int affordabilityScore;
        int reliabilityRiskScore;
        String explanation;
        int communityNeedScore;
    }

    private static final List<Plan> PLAN_LIBRARY = Arrays.asList(
        new Plan("basic", "Basic 100", 100, 35, 1, "light browsing, email, and a small household"),
        new Plan("standard", "Standard 300", 300, 55, 2, "school, streaming, and typical work-from-home use"),
        new Plan("plus", "Plus 500", 500, 75, 3, "larger households, multiple streams, and regular gaming"),
        new Plan("gig", "Gig 1000", 1000, 95, 4, "heavy usage, many devices, and demanding remote work or gaming")
    );

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Broadband Plan Recommender ===");
        System.out.println("Enter the household details below.");
        System.out.println();

        InputData input = new InputData();
        input.monthlyBudget = readDouble(scanner, "Monthly budget: ");
        input.householdSize = readInt(scanner, "Household size: ");
        input.connectedDevices = readInt(scanner, "Number of connected devices: ");

        System.out.println();
        System.out.println("Main uses (answer y/n):");
        input.school = readBoolean(scanner, "School? ");
        input.remoteWork = readBoolean(scanner, "Remote work? ");
        input.streaming = readBoolean(scanner, "Streaming? ");
        input.gaming = readBoolean(scanner, "Gaming? ");
        input.browsing = readBoolean(scanner, "Browsing? ");

        System.out.println();
        input.reliabilityImportant = readBoolean(scanner, "Is reliability important? ");
        input.slowSpeeds = readBoolean(scanner, "Currently experiencing slow speeds? ");
        input.outages = readBoolean(scanner, "Currently experiencing outages? ");

        RecommendationResult result = evaluate(input);

        System.out.println();
        System.out.println("=== Recommendation ===");
        System.out.println("Recommended plan: " + result.recommendedPlan.name);
        System.out.println("Backup recommendation: " + result.backupRecommendation);
        System.out.println("Estimated speed need: " + result.estimatedSpeedNeedMbps + " Mbps");
        System.out.println("Affordability score: " + result.affordabilityScore + "/100");
        System.out.println("Reliability risk score: " + result.reliabilityRiskScore + "/100");
        System.out.println("Community need score: " + result.communityNeedScore + "/100");
        System.out.println("Explanation: " + result.explanation);

        scanner.close();
    }

    public static RecommendationResult evaluate(InputData input) {
        RecommendationResult result = new RecommendationResult();

        int estimatedSpeedNeed = getEstimatedSpeedNeed(input);
        Plan recommendedPlan = recommendPlan(estimatedSpeedNeed, input.monthlyBudget, input.reliabilityImportant);
        int affordabilityScore = getAffordabilityScore(recommendedPlan.price, input.monthlyBudget);
        int reliabilityRiskScore = getReliabilityRiskScore(
            input.reliabilityImportant,
            input.slowSpeeds,
            input.outages,
            recommendedPlan.speedMbps,
            estimatedSpeedNeed
        );
        String backupRecommendation = getBackupRecommendation(
            input.reliabilityImportant,
            input.outages,
            input.remoteWork,
            input.school
        );
        int communityNeedScore = getCommunityNeedScore(input, recommendedPlan.price);
        String explanation = buildExplanation(
            recommendedPlan,
            estimatedSpeedNeed,
            affordabilityScore,
            reliabilityRiskScore,
            input,
            backupRecommendation
        );

        result.recommendedPlan = recommendedPlan;
        result.backupRecommendation = backupRecommendation;
        result.estimatedSpeedNeedMbps = estimatedSpeedNeed;
        result.affordabilityScore = affordabilityScore;
        result.reliabilityRiskScore = reliabilityRiskScore;
        result.communityNeedScore = communityNeedScore;
        result.explanation = explanation;

        return result;
    }

    private static int getEstimatedSpeedNeed(InputData input) {
        int speed = 25;

        speed += input.householdSize * 20;
        speed += input.connectedDevices * 8;

        if (input.school) speed += 40;
        if (input.remoteWork) speed += 75;
        if (input.streaming) speed += 120;
        if (input.gaming) speed += 140;
        if (input.browsing) speed += 20;

        if (input.reliabilityImportant) speed += 35;
        if (input.slowSpeeds) speed += 50;
        if (input.outages) speed += 25;

        if (input.householdSize >= 4 && input.streaming) speed += 50;
        if (input.connectedDevices >= 10) speed += 40;
        if (input.remoteWork && input.gaming) speed += 40;

        int rounded = Math.round(speed / 25.0f) * 25;
        return clamp(rounded, 50, 1500);
    }

    private static Plan recommendPlan(int estimatedSpeedNeed, double budget, boolean reliabilityImportant) {
        List<Plan> affordablePlans = new ArrayList<>();
        for (Plan plan : PLAN_LIBRARY) {
            if (plan.price <= budget) {
                affordablePlans.add(plan);
            }
        }

        for (Plan plan : affordablePlans) {
            if (plan.speedMbps >= estimatedSpeedNeed) {
                return plan;
            }
        }

        if (!affordablePlans.isEmpty()) {
            return affordablePlans.get(affordablePlans.size() - 1);
        }

        if (reliabilityImportant) {
            for (Plan plan : PLAN_LIBRARY) {
                if (plan.speedMbps >= estimatedSpeedNeed) {
                    return plan;
                }
            }
            return PLAN_LIBRARY.get(PLAN_LIBRARY.size() - 1);
        }

        return PLAN_LIBRARY.get(0);
    }

    private static String getBackupRecommendation(boolean reliabilityImportant, boolean outages, boolean remoteWork, boolean school) {
        if (outages || (reliabilityImportant && (remoteWork || school))) {
            return "Consider a mobile hotspot or fixed wireless backup for outages and important work or school hours.";
        }
        return "No backup connection is strongly needed right now.";
    }

    private static int getAffordabilityScore(double planPrice, double budget) {
        if (budget <= 0) {
            return 0;
        }

        double ratio = planPrice / budget;
        if (ratio <= 0.45) return 95;
        if (ratio <= 0.60) return 82;
        if (ratio <= 0.75) return 68;
        if (ratio <= 0.90) return 52;
        return 30;
    }

    private static int getReliabilityRiskScore(
        boolean reliabilityImportant,
        boolean slowSpeeds,
        boolean outages,
        int planSpeed,
        int estimatedSpeedNeed
    ) {
        int risk = 15;

        if (reliabilityImportant) risk += 20;
        if (slowSpeeds) risk += 25;
        if (outages) risk += 30;
        if (planSpeed < estimatedSpeedNeed) risk += 20;
        if (planSpeed >= estimatedSpeedNeed * 1.25) risk -= 10;

        return clamp(risk, 0, 100);
    }

    private static int getCommunityNeedScore(InputData input, double planPrice) {
        int score = 20;

        if (input.monthlyBudget < 50) score += 20;
        else if (input.monthlyBudget < 75) score += 10;

        if (input.householdSize >= 4) score += 15;
        else if (input.householdSize >= 2) score += 8;

        if (input.connectedDevices >= 8) score += 10;
        if (input.school) score += 12;
        if (input.remoteWork) score += 12;
        if (input.streaming || input.gaming) score += 6;
        if (input.reliabilityImportant) score += 8;
        if (input.slowSpeeds) score += 12;
        if (input.outages) score += 18;
        if (planPrice > input.monthlyBudget) score += 15;

        return clamp(score, 0, 100);
    }

    private static String buildExplanation(
        Plan plan,
        int estimatedSpeedNeed,
        int affordabilityScore,
        int reliabilityRiskScore,
        InputData input,
        String backupRecommendation
    ) {
        List<String> uses = new ArrayList<>();
        if (input.school) uses.add("school");
        if (input.remoteWork) uses.add("remote work");
        if (input.streaming) uses.add("streaming");
        if (input.gaming) uses.add("gaming");
        if (input.browsing) uses.add("browsing");

        String usesText = uses.isEmpty() ? "basic internet use" : String.join(", ", uses);

        String affordabilityText;
        if (affordabilityScore >= 80) {
            affordabilityText = "fits comfortably in the budget";
        } else if (affordabilityScore >= 60) {
            affordabilityText = "is workable for the budget";
        } else {
            affordabilityText = "may stretch the budget";
        }

        String reliabilityText;
        if (reliabilityRiskScore <= 35) {
            reliabilityText = "Reliability risk looks fairly low";
        } else if (reliabilityRiskScore <= 65) {
            reliabilityText = "Reliability risk is moderate";
        } else {
            reliabilityText = "Reliability risk is elevated";
        }

        return "We recommend " + plan.name
            + " because your home likely needs about " + estimatedSpeedNeed + " Mbps for " + usesText + ". "
            + "This plan " + affordabilityText + " and is best suited for " + plan.bestFor + ". "
            + reliabilityText + ". "
            + backupRecommendation;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static boolean readBoolean(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim().toLowerCase();
            if (line.equals("y") || line.equals("yes") || line.equals("true")) {
                return true;
            }
            if (line.equals("n") || line.equals("no") || line.equals("false")) {
                return false;
            }
            System.out.println("Please answer y or n.");
        }
    }
}

}
