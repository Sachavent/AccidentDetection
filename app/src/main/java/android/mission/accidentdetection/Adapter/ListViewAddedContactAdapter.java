package android.mission.accidentdetection.Adapter;

import android.app.Activity;
import android.mission.accidentdetection.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Annick on 05/02/2017.
 */

public class ListViewAddedContactAdapter extends BaseAdapter {
    private final ArrayList<String> mData;

    public ListViewAddedContactAdapter(ArrayList<String> contactsName) {
        mData = new ArrayList<>();
        mData.addAll(contactsName);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) parent.getContext()).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_contact_added, parent, false);

            // well set up the ViewHolder
            viewHolder = new ContactViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.nameContactAdded);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.corbeille);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ContactViewHolder) convertView.getTag();
        }


        String contact = getItem(position).toString();

        if (contact != null) {
            viewHolder.name.setText(contact);
        }

        return convertView;
    }


    private class ContactViewHolder{
        public TextView name;
        public ImageView icon;
    }
}
