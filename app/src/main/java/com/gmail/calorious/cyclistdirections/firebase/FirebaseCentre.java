package com.gmail.calorious.cyclistdirections.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gmail.calorious.cyclistdirections.LoginScreenActivity;
import com.gmail.calorious.cyclistdirections.generic.Room;
import com.gmail.calorious.cyclistdirections.generic.RoomSnapshot;
import com.gmail.calorious.cyclistdirections.generic.User;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public static String getUserUUID(int phoneNumber) {
        AtomicReference<String> uuid = new AtomicReference<>();
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "Searching for User UUID from " + phoneNumber + "...");
                for(DataSnapshot uuidSnapshot : snapshot.getChildren()) {
                    // Resolve User from uuid snapshot
                    String stringUUID = uuidSnapshot.getKey();
                    Log.d(TAG, " -- Resolving Key: " + stringUUID);
                    User u = uuidSnapshot.getValue(User.class);
                    if(u == null) {
                        Log.d(TAG, " --> Skipping key as the associated User could not be found.");
                        continue;
                    }
                    int pN = u.getPhoneNumber();
                    Log.d(TAG, " -- Resolving Phone Number from User object: " + pN); // TODO [END OF DEBUG MODE] These debug messages must be removed as it contains phone numbers.
                    if(phoneNumber != pN) {
                        Log.d(TAG, " --> Skipping key as the phone number does not match the query.");
                        continue;
                    }
                    Log.d(TAG, " --> Matching phone number found, utilising the UUID...");
                    uuid.set(stringUUID);
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error code " + error.getCode() + " has occurred while attempting to search for a user's UUID.", error.toException());
            }
        });
        return uuid.get();
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
}
