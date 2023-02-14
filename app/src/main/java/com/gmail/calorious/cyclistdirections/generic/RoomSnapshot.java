package com.gmail.calorious.cyclistdirections.generic;

import java.util.List;

// RoomSnapshot is a class that contains a snapshot of the room data that the database holds.
// The data cannot be converted into a Room object, as the database lacks the knowledge of whether the room has closed as well as any user changes (join/leave)
public class RoomSnapshot {
    private String roomCode;
    private boolean started;
    private List<User> users;
    public RoomSnapshot(String roomCode, boolean started, List<User> users) {
        this.roomCode = roomCode;
        this.started = started;
        this.users = users;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public boolean started() {
        return started;
    }

    public List<User> getUsers() {
        return users;
    }
}
