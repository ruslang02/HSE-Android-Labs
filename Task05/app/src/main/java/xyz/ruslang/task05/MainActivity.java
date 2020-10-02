package xyz.ruslang.task05;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Pattern;

public class MainActivity extends ListActivity {
    public class MyAdapter extends ArrayAdapter<String> {
        public MyAdapter(Context context, int resource,
                         int textViewResourceId, String[] string){
            super(context, resource, textViewResourceId, string);
        }
        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.list_item, parent, false);
            String[] items =
                    getResources().getStringArray(R.array.contacts);
            String[] fields = items[position].split(Pattern.quote("|"), 3);

            ImageView image = (ImageView)
                    row.findViewById(R.id.imageView);

            TextView text = (TextView)
                    row.findViewById(R.id.textView);
            text.setText(fields[0]);

            TextView text2 = (TextView)
                    row.findViewById(R.id.textView2);
            text2.setText(fields[1]);
            switch(fields[2]) {
                case "Moscow":
                case "Sayanogorsk":
                    image.setImageResource(R.drawable.flag_ru);
                    break;
                case "Berlin":
                    image.setImageResource(R.drawable.flag_de);
                    break;
            }
            return row;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListAdapter(new MyAdapter(this, R.layout.list_item, R.id.textView, getResources().getStringArray(R.array.contacts)));
    }
}
