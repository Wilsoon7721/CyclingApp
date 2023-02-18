package com.gmail.calorious.cyclistdirections.generic;

import java.util.HashMap;

public class RoomManager {
    private static HashMap<String, Room> rooms = new HashMap<>();

    public static void updateRoom(String roomCode, Room room) {
        rooms.put(roomCode, room);
    }

    public static void removeRoom(String roomCode) {
        rooms.remove(roomCode);
    }

    public static Room getRoom(String roomCode) {
        return rooms.get(roomCode);
    }
}
