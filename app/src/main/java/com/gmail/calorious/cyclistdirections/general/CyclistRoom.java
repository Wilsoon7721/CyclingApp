package com.gmail.calorious.cyclistdirections.general;

import java.util.HashSet;
import java.util.Set;

public class CyclistRoom {
    /*
     * General class for an active room
     */
    private long id;
    private Set<User> activeUsers = new HashSet<>();
    private boolean started = false;

    public void started(boolean b) {
        started = b;
    }

    public boolean started() {
        return started;
    }

    public Set<User> getActiveUsers() {
        return activeUsers;
    }

    public long getId() {
        return id;
    }

    public void addUser(User user) {
        if(activeUsers.contains(user)) return;
        activeUsers.add(user);
    }

    public void removeUser(User user) {
        if (!activeUsers.contains(user)) return;
        activeUsers.remove(user);
    }

    // Same implementation, different parameter
    public void addUser(long uniqueId, String macAddress, String androidId) {
        User user = new User(uniqueId, macAddress, androidId);
        if(activeUsers.contains(user)) return;
        activeUsers.add(user);
    }

    // Same implementation, different parameter
    public void removeUserByUniqueIdentifier(long uniqueId) {
        for(User user : activeUsers) {
            if(user.getUniqueIdentifier() == uniqueId) {
                activeUsers.remove(user);
                break;
            }
        }
    }

    // Same implementation, different parameter
    public void removeUserByMACAddress(String macAddress) {
        for(User user : activeUsers) {
            if(user.getMacAddress().equals(macAddress)) {
                activeUsers.remove(user);
                break;
            }
        }
    }

    // Same implementation, different parameter
    public void removeUserByAndroidId(String androidId) {
        for(User user : activeUsers) {
            if(user.getAndroidId().equals(androidId)) {
                activeUsers.remove(user);
                break;
            }
        }
    }

}
