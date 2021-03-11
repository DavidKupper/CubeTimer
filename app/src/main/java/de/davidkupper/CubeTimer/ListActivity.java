package de.davidkupper.CubeTimer;

import android.app.AlertDialog;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView dynamic;
    private List<Attempt> attempts;
    private String currFileName;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dynamic = findViewById(R.id.dynamic);
        FloatingActionButton fab = findViewById(R.id.fab);

        dynamic.setOnItemClickListener((parent, view, position, id) -> {
            onItemClicked(position);
        });
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        currFileName = getIntent().getStringExtra("currFileName");
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
                        Toast.makeText(this, "Already DNF", Toast.LENGTH_SHORT).show();
                    attempts.get(position).togglePlus2();
                    updateData();
                })
                .setNegativeButton(R.string.dnf, (dialog, whichButton) -> {
                    if(attempts.get(position).isPlus2())
                        Toast.makeText(this, "Already +2", Toast.LENGTH_SHORT).show();
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
}