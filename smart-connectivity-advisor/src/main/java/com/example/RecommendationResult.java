package com.example;

/**
 * Represents the result of the connectivity recommendation process.
 *
 * This class stores the output of the AI recommendation system,
 * including the selected plan, performance scores, and an explanation
 * of why the recommendation was made.
 */
public class RecommendationResult {

    /** The plan recommended by the system */
    private Plan recommendedPlan;

    /** Estimated internet speed needed (in Mbps) */
    private int estimatedSpeedNeed;

    /** Score (0–100) representing how affordable the plan is */
    private int affordabilityScore;

    /** Score (0–100) representing risk of poor performance */
    private int reliabilityRiskScore;

    /** Score (0–100) indicating level of community connectivity need */
    private int communityNeedScore;

    /** Human-readable explanation of the recommendation */
    private String explanation;

    /** Whether a mobile backup connection is recommended */
    private boolean recommendMobileBackup;

    /**
     * Constructs a RecommendationResult with all relevant data.
     */
    public RecommendationResult(Plan recommendedPlan,
                                int estimatedSpeedNeed,
                                int affordabilityScore,
                                int reliabilityRiskScore,
                                int communityNeedScore,
                                String explanation,
                                boolean recommendMobileBackup) {
        this.recommendedPlan = recommendedPlan;
        this.estimatedSpeedNeed = estimatedSpeedNeed;
        this.affordabilityScore = affordabilityScore;
        this.reliabilityRiskScore = reliabilityRiskScore;
        this.communityNeedScore = communityNeedScore;
        this.explanation = explanation;
        this.recommendMobileBackup = recommendMobileBackup;
    }

    /** Returns the recommended plan */
    public Plan getRecommendedPlan() {
        return recommendedPlan;
    }

    /** Returns the estimated speed requirement in Mbps */
    public int getEstimatedSpeedNeed() {
        return estimatedSpeedNeed;
    }

    /** Returns the affordability score */
    public int getAffordabilityScore() {
        return affordabilityScore;
    }

    /** Returns the reliability risk score */
    public int getReliabilityRiskScore() {
        return reliabilityRiskScore;
    }

    /** Returns the community need score */
    public int getCommunityNeedScore() {
        return communityNeedScore;
    }

    /** Returns the explanation of the recommendation */
    public String getExplanation() {
        return explanation;
    }

    /** Returns whether mobile backup is recommended */
    public boolean isRecommendMobileBackup() {
        return recommendMobileBackup;
    }

    @Override
    public String toString() {
        return "Recommended Plan: " + recommendedPlan + "\n" +
               "Estimated Speed Need: " + estimatedSpeedNeed + " Mbps\n" +
               "Affordability Score: " + affordabilityScore + "\n" +
               "Reliability Risk Score: " + reliabilityRiskScore + "\n" +
               "Community Need Score: " + communityNeedScore + "\n" +
               "Mobile Backup Recommended: " + recommendMobileBackup + "\n" +
               "Explanation: " + explanation;
    }
}