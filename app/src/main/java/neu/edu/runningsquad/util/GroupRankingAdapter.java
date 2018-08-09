package neu.edu.runningsquad.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import neu.edu.runningsquad.R;
import neu.edu.runningsquad.model.Squad;

public class GroupRankingAdapter extends ArrayAdapter<Squad> {
    public GroupRankingAdapter(Context context, List<Squad> squads) {
        super(context, 0, squads);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Squad squad = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.score_group_name);
        name.setText(squad.getName());

        TextView number = convertView.findViewById(R.id.score_group_member_num);
        number.setText(squad.getNumber() + " members");



        TextView points = convertView.findViewById(R.id.score_group_member_stars);
        points.setText("100 stars");

        TextView city = convertView.findViewById(R.id.score_group_city);
        city.setText(squad.getCity());


        return convertView;
    }
}
