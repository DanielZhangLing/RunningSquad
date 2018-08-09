package neu.edu.runningsquad.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import neu.edu.runningsquad.R;
import neu.edu.runningsquad.model.Record;

public class RecordAdapter extends ArrayAdapter<Record> {
    public RecordAdapter(Context context, List<Record> records) {
        super(context, 0, records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Record record = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.record_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.record_challenge_name);
        name.setText(record.getChallenge());

        TextView date = convertView.findViewById(R.id.score_group_member_num);
        date.setText(record.getDateTime().toString());

        TextView points = convertView.findViewById(R.id.record_points);
        points.setText(Integer.toString(record.getReceivedPrize()));


        return convertView;
    }
}
