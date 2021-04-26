package com.thathustudio.spage.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.thathustudio.spage.R;
import com.thathustudio.spage.model.Subject;
import com.thathustudio.spage.utils.SubjectIconFactory;

import java.util.List;

/**
 * Created by Phung on 18/01/2017.
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private final SubjectIconFactory subjectIconFactory;
    private List<Subject> subjectList;
    private Activity context;
    private OnSubjectItemClickListener listener;

    public interface OnSubjectItemClickListener {
        void onItemClick(int position);

        void onSubscribeChange(int position, boolean subscribed);
    }

    public void setOnItemClickListener(OnSubjectItemClickListener listener) {
        this.listener = listener;
    }


    public SubjectAdapter(Activity context, List<Subject> subjects) {
        this.context = context;
        this.subjectList = subjects;
        subjectIconFactory = new SubjectIconFactory(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject_list, parent, false);
        return new ViewHolder(itemView);
    }

    String[] fakeBg = new String[]{"#8BC34A", "#0E7886", "#FF757C", "#0E7886", "#FF757C", "#0E7886"};
    int f = 0;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ivSubjectIcon.setImageDrawable(subjectIconFactory.getSubjectIcon(subjectList.get(position).getId()));
        holder.tvSubjectName.setText(subjectList.get(position).getName());


        holder.ivSubjectIcon.setBackgroundColor(Color.parseColor(fakeBg[f]));
        f++;
        if (f == 6)
            f = 0;
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSubjectIcon;
        TextView tvSubjectName;
        ImageButton btnSubscribe;

        public ViewHolder(View itemView) {
            super(itemView);

            ivSubjectIcon = (ImageView) itemView.findViewById(R.id.ivSubjectIcon);
            tvSubjectName = (TextView) itemView.findViewById(R.id.tvSubjectName);
            btnSubscribe = (ImageButton) itemView.findViewById(R.id.btnSubscribe);

            ivSubjectIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(getAdapterPosition());
                }
            });

            tvSubjectName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(getAdapterPosition());

                }
            });

            btnSubscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (btnSubscribe.isSelected()) {
                        btnSubscribe.setSelected(false);
                        listener.onSubscribeChange(getAdapterPosition(), false);
                        YoYo.with(Techniques.Tada).duration(1500).playOn(btnSubscribe);
                    }
                    else {
                        btnSubscribe.setSelected(true);
                        listener.onSubscribeChange(getAdapterPosition(), true);
                        YoYo.with(Techniques.Tada).duration(1500).playOn(btnSubscribe);
                    }
                }
            });
        }
    }
}
