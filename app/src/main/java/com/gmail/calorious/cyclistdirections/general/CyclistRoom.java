package com.gmail.calorious.cyclistdirections.general;

import java.util.HashSet;
import java.util.Set;

public class CyclistRoom {
    /*
     * General class for an active room
     */
    private long id;
    private Set<User> activeUsers = new HashSet<>();
}
