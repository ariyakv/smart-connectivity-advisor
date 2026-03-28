package com.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Provides rule-based recommendation logic for selecting the best connectivity plan.
 *
 * This service analyzes a user's profile, estimates their internet needs,
 * selects an appropriate plan, calculates helpful scores, and generates
 * a simple explanation.
 */
public class ConnectivityAIService {

    /** List of available plans */
    private final List<Plan> plans = new ArrayList<>();

    /**
     * Initializes the service with mock plan data.
     */
    public ConnectivityAIService() {
        plans.add(new Plan("Basic Connect", 30, 100, false));
        plans.add(new Plan("Family Connect", 55, 300, false));
        plans.add(new Plan("Power Connect", 80, 1000, false));
        plans.add(new Plan("Power Connect + Mobile Backup", 95, 1000, true));
    }

    /**
     * Generates a recommendation result based on the user profile.
     */
    public RecommendationResult recommend(UserProfile profile) {
        int speedNeed = estimateSpeedNeed(profile);
        Plan recommendedPlan = choosePlan(profile, speedNeed);
        boolean mobileBackup = shouldRecommendMobileBackup(profile);

        int affordabilityScore = calculateAffordabilityScore(profile, recommendedPlan);
        int reliabilityRiskScore = calculateReliabilityRiskScore(speedNeed, recommendedPlan, profile);
        int communityNeedScore = calculateCommunityNeedScore(profile, affordabilityScore, reliabilityRiskScore);

        String explanation = generateExplanation(profile, recommendedPlan, speedNeed, mobileBackup);

        return new RecommendationResult(
                recommendedPlan,
                speedNeed,
                affordabilityScore,
                reliabilityRiskScore,
                communityNeedScore,
                explanation,
                mobileBackup
        );
    }

    /**
     * Estimates required internet speed based on usage and household size.
     */
    private int estimateSpeedNeed(UserProfile profile) {
        int speed = 50;

        speed += profile.getHouseholdSize() * 25;
        speed += profile.getDeviceCount() * 10;

        Set<UsageType> usage = profile.getUsageTypes();

        if (usage.contains(UsageType.SCHOOL)) speed += 50;
        if (usage.contains(UsageType.REMOTE_WORK)) speed += 100;
        if (usage.contains(UsageType.STREAMING)) speed += 100;
        if (usage.contains(UsageType.GAMING)) speed += 150;
        if (usage.contains(UsageType.BROWSING)) speed += 20;

        if (profile.isExperiencingSlowSpeeds()) speed += 50;

        return Math.min(speed, 1000);
    }

    /**
     * Selects the best plan based on speed requirement and budget.
     */
    private Plan choosePlan(UserProfile profile, int speedNeed) {
        List<Plan> sortedPlans = plans.stream()
                .sorted(Comparator.comparingInt(Plan::getSpeedMbps))
                .toList();

        Plan bestFit = null;

        for (Plan plan : sortedPlans) {
            if (plan.getSpeedMbps() >= speedNeed) {
                bestFit = plan;
                break;
            }
        }

        if (bestFit == null) {
            bestFit = sortedPlans.get(sortedPlans.size() - 1);
        }

        // If within budget, return it
        if (bestFit.getMonthlyPrice() <= profile.getMonthlyBudget()) {
            return bestFit;
        }

        // Otherwise, find best affordable option
        Plan fallback = null;
        for (Plan plan : sortedPlans) {
            if (plan.getMonthlyPrice() <= profile.getMonthlyBudget()) {
                fallback = plan;
            }
        }

        return (fallback != null) ? fallback : sortedPlans.get(0);
    }

    /**
     * Determines if a mobile backup is recommended.
     */
    private boolean shouldRecommendMobileBackup(UserProfile profile) {
        return profile.isReliabilityImportant() || profile.isExperiencingOutages();
    }

    /**
     * Calculates how affordable the recommended plan is.
     */
    private int calculateAffordabilityScore(UserProfile profile, Plan plan) {
        int budget = profile.getMonthlyBudget();
        int price = plan.getMonthlyPrice();

        if (price <= budget) {
            double ratio = (double) price / budget;
            return Math.max(0, 100 - (int)(ratio * 70));
        } else {
            int overBudget = price - budget;
            return Math.max(0, 40 - overBudget);
        }
    }

    /**
     * Calculates reliability risk based on mismatch between needs and plan.
     */
    private int calculateReliabilityRiskScore(int speedNeed, Plan plan, UserProfile profile) {
        int risk = 0;

        if (plan.getSpeedMbps() < speedNeed) risk += 50;
        if (profile.isExperiencingOutages()) risk += 20;
        if (profile.isReliabilityImportant()) risk += 10;

        if (profile.getUsageTypes().contains(UsageType.REMOTE_WORK)) risk += 10;
        if (profile.getUsageTypes().contains(UsageType.GAMING)) risk += 10;

        return Math.min(risk, 100);
    }

    /**
     * Calculates community need score.
     */
    private int calculateCommunityNeedScore(UserProfile profile, int affordabilityScore, int reliabilityRiskScore) {
        int score = 0;

        if (profile.getMonthlyBudget() < 50) score += 30;
        if (profile.getHouseholdSize() >= 4) score += 20;
        if (profile.getUsageTypes().contains(UsageType.SCHOOL)) score += 20;
        if (affordabilityScore < 50) score += 20;
        if (reliabilityRiskScore > 50) score += 20;

        return Math.min(score, 100);
    }

    /**
     * Generates a simple explanation for the recommendation.
     */
    private String generateExplanation(UserProfile profile, Plan plan, int speedNeed, boolean mobileBackup) {
        StringBuilder sb = new StringBuilder();

        sb.append("Based on your household size of ")
          .append(profile.getHouseholdSize())
          .append(" and ")
          .append(profile.getDeviceCount())
          .append(" devices, ");

        sb.append(plan.getName())
          .append(" is a good fit. ");

        sb.append("You likely need around ")
          .append(speedNeed)
          .append(" Mbps. ");

        if (plan.getMonthlyPrice() <= profile.getMonthlyBudget()) {
            sb.append("This plan fits within your budget. ");
        } else {
            sb.append("This plan may exceed your budget slightly. ");
        }

        if (mobileBackup) {
            sb.append("A mobile backup is recommended due to reliability concerns.");
        }

        return sb.toString();
    }
}