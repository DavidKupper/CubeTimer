package de.davidkupper.CubeTimer.core;

import android.app.AlertDialog;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import de.davidkupper.CubeTimer.R;

public class ListActivity extends AppCompatActivity {

    private ListView dynamic;
    private List<Attempt> attempts;
    private String currFileName;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        currFileName = getIntent().getStringExtra("currFileName");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currFileName);

        dynamic = findViewById(R.id.dynamic);
        FloatingActionButton fab = findViewById(R.id.fab);

        dynamic.setOnItemClickListener((parent, view, position, id) -> {
            onItemClicked(position);
        });
        fab.setOnClickListener(view -> {
            addTimeDialog();
        });

        attempts = (List<Attempt>) MainActivity.readList(this, currFileName);
        adapter = new CustomListAdapter(this, attempts);
        dynamic.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onItemClicked(int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Modify Attempt")
                .setMessage("How do you want to modify this attempt?")
                .setIcon(R.drawable.ic_baseline_create_24)
                .setPositiveButton(R.string.plus2, (dialog, whichButton) -> {
                    if(attempts.get(position).isDnf())
                        toast("Already DNF");
                    attempts.get(position).togglePlus2();
                    updateData();
                })
                .setNegativeButton(R.string.dnf, (dialog, whichButton) -> {
                    if(attempts.get(position).isPlus2())
                        toast("Already +2");
                    attempts.get(position).toggleDnf();
                    updateData();
                })
                .setNeutralButton(R.string.delete, (dialog, whichButton) -> {
                    deleteTime(position);
                }).show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.transparent, null));
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundColor(getResources().getColor(R.color.transparent, null));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.transparent, null));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue, null));
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.red, null));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue, null));

    }

    private void updateData() {
        MainActivity.saveList(this, attempts, currFileName);
        adapter.notifyDataSetChanged();
    }


    private void deleteTime(int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Delete Time")
                .setMessage("Do you really want to delete this time?")
                .setIcon(android.R.drawable.ic_delete)
                .setPositiveButton(R.string.delete, (dialog, whichButton) -> {
                    attempts.remove(position);
                    updateData();
                })
                .setNegativeButton(R.string.cancel, null).show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.transparent, null));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.transparent, null));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red, null));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.red, null));
    }

    private void addTimeDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Add Time")
                .setView(R.layout.add_time)
                .setMessage("Enter time to add: ")
                .setIcon(R.drawable.ic_baseline_add_alarm_24)
                .setPositiveButton(R.string.add, (dialog, whichButton) -> {
                    EditText editText = findViewById(R.id.add_time_edit);
                    //EditText scramble = findViewById(R.id.add_time_scramble_edit);
                    addTime(editText.getText().toString(), "");//scramble.getText().toString());
                })
                .setNegativeButton(R.string.cancel, null).show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.transparent, null));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.transparent, null));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue, null));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.red, null));
    }

    // TODO fix this shit
    private void addTime(String enteredText, String scramble) {
        int doublePointIdx = enteredText.indexOf(":");
        int pointIdx = enteredText.indexOf(".");
        if(pointIdx == -1 || doublePointIdx == -1 || doublePointIdx > pointIdx) {
            toast("Time not valid!");
            Log.d("addTimer", "-1");
        }
        else {
            try {
                int min = Integer.parseInt(enteredText.substring(0, doublePointIdx));
                int sec = Integer.parseInt(enteredText.substring(doublePointIdx + 1, pointIdx));
                int millis = Integer.parseInt(enteredText.substring(pointIdx + 1));
                long time = millis + sec * 1000 + min * 60000;
                attempts.add(new Attempt(time, scramble));
                updateData();
                Log.d("addTime", "success");
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
                Log.d("addTime", "invalid");
                toast("Time not valid!");
            }
        }
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}