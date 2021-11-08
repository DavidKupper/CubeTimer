package de.davidkupper.CubeTimer.core;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.davidkupper.CubeTimer.R;

public class CustomListAdapter extends ArrayAdapter<Attempt> {
    private final Context context;
    private final List<Attempt> values;

    public CustomListAdapter(Context context, List<Attempt> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        final TextView firstLine = rowView.findViewById(R.id.firstLine);
        final TextView secondLine = rowView.findViewById(R.id.secondLine);
        int number = values.size() - position;
        firstLine.setText(number + ". " + values.get(position).getTimeString());
        setTextViewStrikeThrough(firstLine, values.get(position).isDnf());
        secondLine.setText(values.get(position).getScramble());
        return rowView;
    }

    private void setTextViewStrikeThrough(TextView view, boolean strikeThrough) {
        if (strikeThrough) {
            view.setPaintFlags(view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            view.setTextColor(context.getResources().getColor(R.color.red, null));
        }
    }
}

