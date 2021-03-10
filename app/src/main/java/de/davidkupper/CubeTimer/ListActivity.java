package de.davidkupper.CubeTimer;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView dynamic;
    private List<Attempt> attempts;

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
            onItemClicked();
        });
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        String currFileName = getIntent().getStringExtra("currFileName");
        attempts = (List<Attempt>) MainActivity.readList(this, currFileName);
        CustomListAdapter adapter = new CustomListAdapter(this, attempts);
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

    private void onItemClicked() {

    }
}