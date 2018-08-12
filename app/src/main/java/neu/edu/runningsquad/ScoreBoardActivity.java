package neu.edu.runningsquad;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import neu.edu.runningsquad.model.Squad;
import neu.edu.runningsquad.util.GroupRankingAdapter;

public class ScoreBoardActivity extends MainActivity {

    private ListView listView;
    private List<Squad> squadList = new ArrayList<>();
    private GroupRankingAdapter groupRankingAdapter;
    private EditText cityNameText;
    private DatabaseReference mReference;
    private final String INITIALIZED_CITY = "Boston";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_score_board, contentFrameLayout);
        mReference = FirebaseDatabase.getInstance().getReference();

        cityNameText = this.findViewById(R.id.score_board_city);

        groupRankingAdapter = new GroupRankingAdapter(this, squadList);
        listView = findViewById(R.id.score_board_list_view);
        listView.setAdapter(groupRankingAdapter);

        cityNameText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.i("Runner", v.getText().toString());
                    searchForGroups(v.getText().toString());
                }
                return false;
            }
        });
        searchForGroups(INITIALIZED_CITY);
    }


    void searchForGroups(String city) {
        mReference.child("squads").orderByChild("city").equalTo(city).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Squad> newData = new ArrayList<Squad>();
                for (DataSnapshot recordSnapshot : dataSnapshot.getChildren()) {

                    Squad squad = recordSnapshot.getValue(Squad.class);
                    newData.add(squad);

                }

                Collections.sort(newData, new Comparator<Squad>() {
                    @Override
                    public int compare(Squad lhs, Squad rhs) {
                        return lhs.getTotalStars() > rhs.getTotalStars() ? -1 : (lhs.getTotalStars() < rhs.getTotalStars()) ? 1 : 0;
                    }
                });
                squadList.clear();
                squadList.addAll(newData);
                groupRankingAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
