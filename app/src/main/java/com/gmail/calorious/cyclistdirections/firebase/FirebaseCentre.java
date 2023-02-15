package com.gmail.calorious.cyclistdirections.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gmail.calorious.cyclistdirections.LoginScreenActivity;
import com.gmail.calorious.cyclistdirections.generic.Room;
import com.gmail.calorious.cyclistdirections.generic.RoomSnapshot;
import com.gmail.calorious.cyclistdirections.generic.User;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class FirebaseCentre {
    public static final String TAG = "Firebase";
    private static FirebaseDatabase database;
    private static DatabaseReference usersRef, roomsRef;

    static {
        database = FirebaseDatabase.getInstance("https://cyclistdirections-default-rtdb.asia-southeast1.firebasedatabase.app");
        usersRef = database.getReference("users");
        roomsRef = database.getReference("rooms");
    }

    public static DatabaseReference getUsersReference() {
        return usersRef;
    }

    public static DatabaseReference getRoomsReference() {
        return roomsRef;
    }

    // Returns whether adding this user was successful.
    public static boolean addUser(User user) {
        UUID uuid = user.getUUID();
        AtomicBoolean success = new AtomicBoolean();
        usersRef.child(uuid.toString()).setValue(user).addOnCompleteListener((task) -> {
            success.set(task.isSuccessful());
            if (task.isSuccessful()) {
                Log.d(TAG, "Successfully added a new User [" + user.getName() + "] with UUID '" + uuid + "' to the database.");
                return;
            }
            Log.e(TAG, "An error occurred while adding a new user to the database.", task.getException());
        });
        return success.get();
    }

    // Database: enquire user state
    public static User getUser(String uuid) {
        AtomicReference<User> user = new AtomicReference<>();
        usersRef.child(uuid).get().addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Successfully retrieved a User from the database.");
                user.set(task.getResult().getValue(User.class));
                return;
            }
            Log.e(TAG, "An error occurred while retrieving a user from the database.", task.getException());
            user.set(null);
        });
        return user.get();
    }

    public static boolean deleteUser(String uuid) {
        AtomicBoolean success = new AtomicBoolean();
        usersRef.child(uuid).removeValue().addOnCompleteListener((task) -> {
            success.set(task.isSuccessful());
            if(task.isSuccessful()) {
                Log.d(TAG, "Successfully deleted User with UUID '" + uuid + "' from the database.");
                return;
            }
            Log.e(TAG, "An error occurred while deleting User with UUID '" + uuid + "' from the database.", task.getException());
        });
        return success.get();
    }

    // Returns nothing.
    public static void addRoom(Room room) {
        String roomCode = room.getCode();
        DatabaseReference currentRoomRef = roomsRef.child(roomCode);
        currentRoomRef.child("roomCode").setValue(roomCode);
        currentRoomRef.child("started").setValue(room.sessionStarted());
        currentRoomRef.child("users").setValue(room.getUsers());
    }

    // Database: enquire room state
    @SuppressWarnings("unchecked")
    public static RoomSnapshot getRoomData(String roomCode) {
        AtomicBoolean started = new AtomicBoolean();
        AtomicReference<List<User>> users = new AtomicReference<>();
        Boolean a = true;
        DatabaseReference currentRoomRef = roomsRef.child(roomCode);
        currentRoomRef.child("started").get().addOnCompleteListener(task -> {
           if(task.isSuccessful())
                started.set(Boolean.parseBoolean(task.getResult().getValue(String.class)));
           else
               Log.e(TAG, "An error occurred while retrieving a room snapshot of '" + roomCode + "'.", task.getException());
        });
        currentRoomRef.child("users").get().addOnCompleteListener(task -> {
           if(task.isSuccessful())
               users.set((List<User>) task.getResult().getValue());
           else
               Log.e(TAG, "An error occurred while retrieving a room snapshot of '" + roomCode + "'.", task.getException());
        });
        return new RoomSnapshot(roomCode, started.get(), users.get());
    }

    public static boolean deleteRoom(String joinCode) {
        AtomicBoolean success = new AtomicBoolean();
        roomsRef.child(joinCode).removeValue().addOnCompleteListener((task) -> {
            success.set(task.isSuccessful());
            if(task.isSuccessful()) {
                Log.d(TAG, "Successfully deleted Room with code '" + joinCode + "'.");
                return;
            }
            Log.e(TAG, "An error occurred while deleting Room with code '" + joinCode + "'.", task.getException());
        });
        return success.get();
    }

    // Start verification for a phone number
    // Note: Phone Number Format should follow "65XXXXXXXX"
    // Note 2: Timeout in seconds should follow a countdown inside login_screen.xml
    public static void startNumberVerification(String phoneNumber, long timeoutSeconds) {
        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder().setPhoneNumber(phoneNumber).setTimeout(timeoutSeconds,TimeUnit.SECONDS).setCallbacks(LoginScreenActivity.getDefaultCallback()).build();
        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }
}
