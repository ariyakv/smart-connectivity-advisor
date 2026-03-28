package com.example;

/**
 * Represents an internet or connectivity plan that can be recommended to a user.
 *
 * A Plan contains key attributes such as:
 * - name: the name of the plan (e.g., "Basic Connect")
 * - monthlyPrice: the cost per month in dollars
 * - speedMbps: the internet speed in megabits per second
 * - mobileBackupIncluded: whether the plan includes a mobile backup option
 *
 * This class is used by the AI recommendation system to compare plans
 * and determine the best option for a user's needs.
 */
public class Plan {
    private String name;
    private int monthlyPrice;
    private int speedMbps;
    private boolean mobileBackupIncluded;
    
    /**
     * Constructor for Plan. Includes name, monthlyPrice, speedMbps, and 
     * if mobile backup is included or not.
     * @param name
     * @param monthlyPrice
     * @param speedMbps
     * @param mobileBackupIncluded
     */
    public Plan(String name, int monthlyPrice, int speedMbps, boolean mobileBackupIncluded) {
        this.name = name;
        this.monthlyPrice = monthlyPrice;
        this.speedMbps = speedMbps;
        this.mobileBackupIncluded = mobileBackupIncluded;
    } //Plan
    /** Returns this name
     * @return this.name
     */
    public String getName() {
        return name;
    } //getName

    /**
     * Returns monthly price.
     * @return this.monthlyPrice.
     */
    public int getMonthlyPrice() {
        return monthlyPrice;
    } //getMonthlyPrice

    /**
     * Returns mobileBackupIncluded
     * @return this.mobileBackupIncluded
     */
    public boolean isMobileBackupIncluded() {
        return mobileBackupIncluded;
    } //isMobileBackupIncludes

    /**
     * Returns this.speedMbps.
     * @return this.speedMbps.
     */
    public int getSpeedMbps() {
        return speedMbps;
    } //getSpeedMbps
    /**
     * Return string representation of all instance variables using SOUT.
     */
    @Override
    public String toString() {
        return name + "($" + monthlyPrice + "/mo, " + speedMbps + " Mbps)"; 
    }//toString
}//Plan
