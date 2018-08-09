package neu.edu.runningsquad.util;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import neu.edu.runningsquad.GroupInfoActivity;
import neu.edu.runningsquad.R;
import neu.edu.runningsquad.model.Squad;

import static neu.edu.runningsquad.util.Sessions.saveTempInfo;

public class SquadAdapter extends RecyclerView.Adapter<SquadAdapter.ViewHolder> {

    private List<Squad> mData;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView nameView, cityView, memberView;
        private ImageView detailView;

        public ViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.squad_row_name);
            cityView = view.findViewById(R.id.squad_row_city);
            memberView = view.findViewById(R.id.squad_row_member);
            detailView = view.findViewById(R.id.squad_row_detail);
        }
    }

    public SquadAdapter(List<Squad> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public SquadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.squad_row, parent, false);
        return new SquadAdapter.ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SquadAdapter.ViewHolder viewHolder, int i) {
        final Squad squad = mData.get(i);
        viewHolder.nameView.setText(squad.getName());
        viewHolder.cityView.setText(squad.getCity());
        viewHolder.memberView.setText(Integer.toString(squad.getNumber()) + "/10");
        viewHolder.detailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTempInfo("", squad.getName(), view.getContext());
                jump2Person(view);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void jump2Person(View view) {
        Intent intent = new Intent(view.getContext(), GroupInfoActivity.class);
        view.getContext().startActivity(intent);
    }
}
