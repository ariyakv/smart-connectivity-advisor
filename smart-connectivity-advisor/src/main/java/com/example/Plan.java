package com.example;

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
