package com.thathustudio.spage.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thathustudio.spage.R;
import com.thathustudio.spage.model.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Room} and makes a call to the
 * specified {@link OnRoomViewInteractionListener}.
 */
public class RoomRecyclerViewAdapter extends RecyclerView.Adapter<RoomRecyclerViewAdapter.RoomHolder> implements View.OnClickListener {
    private final List<Room> rooms;
    private final List<String> roomKeys;
    private final OnRoomViewInteractionListener listener;
    private final String roomPrefix;
    private final Locale locale;
    private final String nPlayerPrefix;

    public RoomRecyclerViewAdapter(Context context, OnRoomViewInteractionListener listener) {
        this.rooms = new ArrayList<>();
        this.roomKeys = new ArrayList<>();
        this.listener = listener;
        this.locale = Locale.getDefault();
        this.roomPrefix = context.getResources().getString(R.string.room_of);
        this.nPlayerPrefix = context.getResources().getString(R.string.current_number_of_players);
    }

    public void addRoom(String key, String replacedKey, Room room) {
        if (replacedKey != null && !replacedKey.isEmpty()) {
            int index = roomKeys.indexOf(replacedKey);
            roomKeys.add(index, key);
            rooms.add(index, room);
            notifyItemInserted(index);
        } else {
            roomKeys.add(0, key);
            rooms.add(0, room);
            notifyItemInserted(0);
        }
    }

    public void changeRoom(String key, Room room) {
        int index = roomKeys.indexOf(key);
        rooms.set(index, room);
        notifyItemChanged(index);
    }

    public void removeRoom(String key) {
        int index = roomKeys.indexOf(key);
        rooms.remove(index);
        roomKeys.remove(index);
        notifyItemRemoved(index);
    }

    public void moveRoom(String key, String replacedKey, Room room) {
        int index = roomKeys.indexOf(key);
        rooms.remove(index);
        roomKeys.remove(index);
        if (replacedKey != null && !replacedKey.isEmpty()) {
            int replacedIndex = roomKeys.indexOf(replacedKey);
            roomKeys.add(replacedIndex, key);
            rooms.add(replacedIndex, room);
            notifyItemMoved(index, replacedIndex);
        } else {
            roomKeys.add(0, key);
            rooms.add(0, room);
            notifyItemMoved(index, 0);
        }
    }

    @Override
    public RoomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        view.setOnClickListener(this);
        return new RoomHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomHolder holder, int position) {
        holder.room = rooms.get(position);
        holder.itemView.setTag(position);
        holder.textViewRoomOwner.setText(String.format(locale, "%s %s", roomPrefix, holder.room.getOwner()));
        holder.textViewNPlayers.setText(String.format(locale, "%s: %d", nPlayerPrefix, holder.room.getNPlayers()));
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            switch (v.getId()) {
                case R.id.rltLyot_container:
                    int position = (int) v.getTag();
                    listener.onRoomViewClick(roomKeys.get(position), rooms.get(position));
                    break;
            }
        }
    }

    public interface OnRoomViewInteractionListener {
        void onRoomViewClick(String key, Room room);
    }

    public class RoomHolder extends RecyclerView.ViewHolder {
        public Room room;
        public final TextView textViewRoomOwner;
        public final TextView textViewNPlayers;

        public RoomHolder(View view) {
            super(view);
            textViewRoomOwner = (TextView) view.findViewById(R.id.txtV_roomOwner);
            textViewNPlayers = (TextView) view.findViewById(R.id.txtV_roomNPlayers);
        }
    }
}
