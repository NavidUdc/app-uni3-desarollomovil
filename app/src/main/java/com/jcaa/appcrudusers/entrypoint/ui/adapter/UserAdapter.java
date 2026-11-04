package com.jcaa.appcrudusers.entrypoint.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcaa.appcrudusers.R;
import com.jcaa.appcrudusers.application.dto.response.UserResponseDto;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    public interface OnUserClickListener {
        void onUserClick(UserResponseDto user);
    }

    public interface OnUserEditListener {
        void onUserEdit(UserResponseDto user);
    }

    public interface OnUserDeleteListener {
        void onUserDelete(UserResponseDto user);
    }

    private final List<UserResponseDto> users = new ArrayList<>();
    private final OnUserClickListener clickListener;
    private final OnUserEditListener editListener;
    private final OnUserDeleteListener deleteListener;

    public UserAdapter(
            final OnUserClickListener clickListener,
            final OnUserEditListener editListener,
            final OnUserDeleteListener deleteListener
    ) {
        this.clickListener = clickListener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    public void setUsers(final List<UserResponseDto> newUsers) {
        this.users.clear();
        if (newUsers != null) {
            this.users.addAll(newUsers);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
        final UserResponseDto user = users.get(position);
        holder.bind(user, clickListener, editListener, deleteListener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvUserName;
        private final TextView tvUserEmail;
        private final TextView tvUserRole;
        private final TextView tvUserStatus;
        private final ImageButton btnEditUser;
        private final ImageButton btnDeleteUser;

        UserViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserRole = itemView.findViewById(R.id.tvUserRole);
            tvUserStatus = itemView.findViewById(R.id.tvUserStatus);
            btnEditUser = itemView.findViewById(R.id.btnEditUser);
            btnDeleteUser = itemView.findViewById(R.id.btnDeleteUser);
        }

        void bind(
                final UserResponseDto user,
                final OnUserClickListener clickListener,
                final OnUserEditListener editListener,
                final OnUserDeleteListener deleteListener
        ) {
            tvUserName.setText(user.name());
            tvUserEmail.setText(user.email());
            tvUserRole.setText(user.role());
            tvUserStatus.setText(user.status());

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onUserClick(user);
                }
            });

            btnEditUser.setOnClickListener(v -> {
                if (editListener != null) {
                    editListener.onUserEdit(user);
                }
            });

            btnDeleteUser.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onUserDelete(user);
                }
            });
        }
    }
}
