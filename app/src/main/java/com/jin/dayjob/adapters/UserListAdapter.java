package com.jin.dayjob.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jin.dayjob.models.User;
import com.jin.dayjob.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Jin on 2015-06-15.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private final LayoutInflater layout_inflater;
    private ArrayList<User> userList;
    private Context context;
    private ClickListener clickListener;
    private ImageButton.OnClickListener imgBtnClickListener;

    public UserListAdapter(Context context) {
        this.layout_inflater = LayoutInflater.from(context);
        this.context = context;
        this.userList = new ArrayList<>();
    }

    public void setTaskList(ArrayList<User> userList) {
        this.userList = userList;
        notifyItemRangeChanged(0, userList.size());
    }

    public void addItem(User item) {
        userList.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        userList.remove(position);
        notifyItemRemoved(position);
    }

    public User getItemData(int position) {
        return userList.get(position);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layout_inflater.inflate(R.layout.user_info, parent, false);
        UserListViewHolder holder = new UserListViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(UserListViewHolder holder, int position) {
        User currentUser = userList.get(position);
        holder.textView1.setText(currentUser.getName());
        holder.textView2.setText(currentUser.getAddress());
        holder.textView3.setText(currentUser.getPhone());
        holder.textView4.setText(currentUser.getSex());

        Calendar myCalendar = Calendar.getInstance();
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        SimpleDateFormat showFormat = new SimpleDateFormat("yyyy년생", Locale.KOREA);

        try {
            holder.textView5.setText(showFormat.format(parseFormat.parse(currentUser.getBirth())));
        } catch (ParseException e) {

        }


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public interface ClickListener {

        public void itemClick(View view, int position);
    }

    class UserListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
        ImageButton locationBtn;

        public UserListViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.textView1);
            textView2 = (TextView) itemView.findViewById(R.id.textView2);
            textView3 = (TextView) itemView.findViewById(R.id.textView3);
            textView4 = (TextView) itemView.findViewById(R.id.textView4);
            textView5 = (TextView) itemView.findViewById(R.id.textView5);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (clickListener != null) {

                clickListener.itemClick(v, getPosition());
            }
        }
    }
}

