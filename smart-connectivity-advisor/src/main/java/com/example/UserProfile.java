package com.example;

import java.util.Set;

/**
<<<<<<< HEAD
 * Represents the user's household and internet usage profile.
 *
 * This class contains all input data provided by the user, which is used
 * by the recommendation system to determine the most suitable connectivity plan.
 */
public class UserProfile {

    /** Monthly budget in dollars */
    private int monthlyBudget;

    /** Number of people in the household */
    private int householdSize;

    /** Number of devices connected to the internet */
    private int deviceCount;

    /** Types of internet usage (e.g., SCHOOL, STREAMING, GAMING) */
    private Set<UsageType> usageTypes;

    /** Whether a reliable connection is critical (e.g., work or school) */
    private boolean reliabilityImportant;

    /** Whether the user is currently experiencing outages */
    private boolean experiencingOutages;

    /** Whether the user is currently experiencing slow speeds */
    private boolean experiencingSlowSpeeds;

    /**
     * Constructs a UserProfile with all relevant user input data.
     */
    public UserProfile(int monthlyBudget,
                       int householdSize,
                       int deviceCount,
                       Set<UsageType> usageTypes,
                       boolean reliabilityImportant,
                       boolean experiencingOutages,
                       boolean experiencingSlowSpeeds) {
        this.monthlyBudget = monthlyBudget;
        this.householdSize = householdSize;
        this.deviceCount = deviceCount;
        this.usageTypes = usageTypes;
        this.reliabilityImportant = reliabilityImportant;
        this.experiencingOutages = experiencingOutages;
        this.experiencingSlowSpeeds = experiencingSlowSpeeds;
    }

    /** Returns the user's monthly budget */
    public int getMonthlyBudget() {
        return monthlyBudget;
    }

    /** Returns the number of people in the household */
    public int getHouseholdSize() {
        return householdSize;
    }

    /** Returns the number of connected devices */
    public int getDeviceCount() {
        return deviceCount;
    }

    /** Returns the set of usage types */
    public Set<UsageType> getUsageTypes() {
        return usageTypes;
    }

    /** Returns whether reliability is important */
    public boolean isReliabilityImportant() {
        return reliabilityImportant;
    }

    /** Returns whether the user is experiencing outages */
    public boolean isExperiencingOutages() {
        return experiencingOutages;
    }

    /** Returns whether the user is experiencing slow speeds */
    public boolean isExperiencingSlowSpeeds() {
        return experiencingSlowSpeeds;
    }
}
=======
* Represents the user's household and internet usage profile.
*
* This class stores all input data provided by the user, which is used
* by the AI recommendation system to determine the most suitable
* connectivity plan.
*/
public class UserProfile {

/** Monthly budget in dollars */
private int monthlyBudget;

/** Number of people in the household */
private int householdSize;

/** Number of devices connected to the internet */
private int deviceCount;

/** Types of internet usage (e.g., streaming, gaming, school) */
private Set<UsageType> usageTypes;

/** Whether a reliable connection is critical (e.g., for work or school) */
private boolean reliabilityImportant;

/** Whether the user is currently experiencing outages */
private boolean experiencingOutages;

/** Whether the user is currently experiencing slow speeds */
private boolean experiencingSlowSpeeds;

/**
* Constructs a UserProfile with all relevant user inputs.
*/
public UserProfile(int monthlyBudget,
int householdSize,
int deviceCount,
Set<UsageType> usageTypes,
boolean reliabilityImportant,
boolean experiencingOutages,
boolean experiencingSlowSpeeds) {
this.monthlyBudget = monthlyBudget;
this.householdSize = householdSize;
this.deviceCount = deviceCount;
this.usageTypes = usageTypes;
this.reliabilityImportant = reliabilityImportant;
this.experiencingOutages = experiencingOutages;
this.experiencingSlowSpeeds = experiencingSlowSpeeds;
}

/** Returns the user's monthly budget */
public int getMonthlyBudget() {
return monthlyBudget;
}

/** Returns the number of people in the household */
public int getHouseholdSize() {
return householdSize;
}

/** Returns the number of connected devices */
public int getDeviceCount() {
return deviceCount;
}

/** Returns the set of usage types */
public Set<UsageType> getUsageTypes() {
return usageTypes;
}

/** Returns whether reliability is important */
public boolean isReliabilityImportant() {
return reliabilityImportant;
}

/** Returns whether the user is experiencing outages */
public boolean isExperiencingOutages() {
return experiencingOutages;
}

/** Returns whether the user is experiencing slow speeds */
public boolean isExperiencingSlowSpeeds() {
return experiencingSlowSpeeds;
}
}
>>>>>>> c4fa693f6e8a29f7d93e31f5861243cf8e4c0630
