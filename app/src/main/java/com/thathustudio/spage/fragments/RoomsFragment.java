package com.thathustudio.spage.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.thathustudio.spage.R;
import com.thathustudio.spage.activities.RoomActivity;
import com.thathustudio.spage.fragments.dialogs.Task4PromptDialogFragment;
import com.thathustudio.spage.model.Room;
import com.thathustudio.spage.utils.RoomRecyclerViewAdapter;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RoomsFragment extends Fragment implements RoomRecyclerViewAdapter.OnRoomViewInteractionListener, View.OnClickListener, Task4PromptDialogFragment.OnTask4PromptDialogInteractionListener {
    private static final String USER_NAME = "User name";
    private static final String COMFIRM_DIALOG = "Confirm dialog";

    private String userName;
    private DatabaseReference database;
    private RoomRecyclerViewAdapter adapter;
    private RoomsChildListener roomsListener;
    private String currentKey;

    public static RoomsFragment newInstance(String userName) {
        RoomsFragment fragment = new RoomsFragment();
        Bundle args = new Bundle();
        args.putString(USER_NAME, userName);
        fragment.setArguments(args);
        return fragment;
    }

    private void goToRoom(String key, boolean isOwner) {
        Intent intent = new Intent(getContext(), RoomActivity.class);
        intent.putExtra(RoomActivity.ROOM_ID, key);
        intent.putExtra(RoomActivity.USER_NAME, userName);
        intent.putExtra(RoomActivity.IS_OWNER, isOwner);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            userName = getArguments().getString(USER_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rclrV_rooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RoomRecyclerViewAdapter(context, this);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.btn_create).setOnClickListener(this);

        database = FirebaseDatabase.getInstance().getReference();
        roomsListener = new RoomsChildListener(this);
        database.child("hall").addChildEventListener(roomsListener);

        return view;
    }

    @Override
    public void onRoomViewClick(String key, Room room) {
        currentKey = key;
        if (room.getNPlayers() >= 2)
            Toast.makeText(getContext(), "This room is full", Toast.LENGTH_LONG).show();
        else
            Task4PromptDialogFragment.newInstance(Task4PromptDialogFragment.DONT_USE, R.string.go_into_this_room, R.string.go, R.string.cancel).show(getChildFragmentManager(), COMFIRM_DIALOG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                Map<String, Object> initPlayingRoom = new HashMap<>();
                initPlayingRoom.put("player1Name", userName);
                initPlayingRoom.put("player1Score", 0);
                initPlayingRoom.put("currentQuestionIndex", 0);
                initPlayingRoom.put("nPlayersFinishedCurrentQuestion", 0);
                DatabaseReference newRoom = database.child("playingRooms").push();
                newRoom.setValue(initPlayingRoom);

                goToRoom(newRoom.getKey(), true);
                break;
        }
    }

    @Override
    public void onDestroy() {
        database.child("hall").removeEventListener(roomsListener);
        super.onDestroy();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        switch (dialog.getTag()) {
            case COMFIRM_DIALOG:
                database.child("hall").child(currentKey).child("nplayers").runTransaction(new JoinTransaction(this));
                break;
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.v("SPage", String.format(Locale.US, "Cancel dialog: %s", dialog.getTag()));
    }

    public static class JoinTransaction implements Transaction.Handler {
        public WeakReference<RoomsFragment> weakReferenceRoomFragment;

        public JoinTransaction(RoomsFragment roomsFragment) {
            weakReferenceRoomFragment = new WeakReference<>(roomsFragment);
        }

        @Override
        public Transaction.Result doTransaction(MutableData mutableData) {
            Integer nPlayers = mutableData.getValue(Integer.class);
            if (nPlayers == null || nPlayers >= 2) {
                return Transaction.abort();
            }

            mutableData.setValue(2);
            return Transaction.success(mutableData);
        }

        @Override
        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
            if (databaseError != null)
                Log.v("SPage", databaseError.getMessage());

            RoomsFragment roomsFragment = weakReferenceRoomFragment.get();
            if (roomsFragment != null) {
                if (b) {
                    roomsFragment.database.child("playingRooms").child(roomsFragment.currentKey).child("player2Name").setValue(roomsFragment.userName);
                    roomsFragment.database.child("playingRooms").child(roomsFragment.currentKey).child("player2Score").setValue(0);
                    roomsFragment.goToRoom(roomsFragment.currentKey, false);
                } else {
                    Toast.makeText(roomsFragment.getContext(), "Room is destroyed or full", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public static class RoomsChildListener implements ChildEventListener {
        private final WeakReference<RoomsFragment> weakReferenceRoomsFragment;

        public RoomsChildListener(RoomsFragment roomsFragment) {
            this.weakReferenceRoomsFragment = new WeakReference<>(roomsFragment);
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            RoomsFragment roomsFragment = weakReferenceRoomsFragment.get();
            if (roomsFragment != null) {
                roomsFragment.adapter.addRoom(dataSnapshot.getKey(), s, dataSnapshot.getValue(Room.class));
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            RoomsFragment roomsFragment = weakReferenceRoomsFragment.get();
            if (roomsFragment != null) {
                roomsFragment.adapter.changeRoom(dataSnapshot.getKey(), dataSnapshot.getValue(Room.class));
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            RoomsFragment roomsFragment = weakReferenceRoomsFragment.get();
            if (roomsFragment != null) {
                roomsFragment.adapter.removeRoom(dataSnapshot.getKey());
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            RoomsFragment roomsFragment = weakReferenceRoomsFragment.get();
            if (roomsFragment != null) {
                roomsFragment.adapter.moveRoom(dataSnapshot.getKey(), s, dataSnapshot.getValue(Room.class));
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            RoomsFragment roomsFragment = weakReferenceRoomsFragment.get();
            if (roomsFragment != null) {
                Toast.makeText(roomsFragment.getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Log.v("SPage", databaseError.getMessage());
            }
        }
    }
}
