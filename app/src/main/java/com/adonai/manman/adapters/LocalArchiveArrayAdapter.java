package com.adonai.manman.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adonai.manman.R;
import com.adonai.manman.entities.RetrievedLocalManPage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Array adapter for showing files in local man page archive
 * The data retrieval is done through {@link com.adonai.manman.ManLocalArchiveFragment.LocalArchiveParserCallback}
 *
 * @see android.widget.ArrayAdapter
 * @see java.io.File
 * @author Adonai
 */
public class LocalArchiveArrayAdapter extends ArrayAdapter<RetrievedLocalManPage> {
    private List<RetrievedLocalManPage> originals;
    private List<RetrievedLocalManPage> filtered;

    public LocalArchiveArrayAdapter(Context context, int resource, int textViewResourceId, List<RetrievedLocalManPage> objects) {
        super(context, resource, textViewResourceId, objects);
        originals = objects;
        filtered = objects;
    }

    @Override
    public int getCount() {
        return filtered.size();
    }

    @Override
    public RetrievedLocalManPage getItem(int position) {
        return filtered.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final RetrievedLocalManPage current = getItem(position);
        String effectiveName = current.getCommandName();
        View root = super.getView(position, convertView, parent);

        TextView command = (TextView) root.findViewById(R.id.command_name_label);
        command.setText(effectiveName);

        TextView url = (TextView) root.findViewById(R.id.command_description_label);
        url.setText(current.getParentEntry());

        ImageView popup = (ImageView) root.findViewById(R.id.popup_menu);
        popup.setVisibility(View.GONE); // save for future, hide for now

        return root;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults fr = new FilterResults();
                if(TextUtils.isEmpty(constraint)) { // special case for empty filter
                    fr.values = originals;
                    fr.count = originals.size();
                    return fr;
                }

                List<RetrievedLocalManPage> tempFilteredValues = new ArrayList<>();
                for(RetrievedLocalManPage f : originals) {
                    if(f.getCommandName().startsWith(constraint.toString())) {
                        tempFilteredValues.add(f);
                    }
                }

                fr.values = tempFilteredValues;
                fr.count = tempFilteredValues.size();
                return fr;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filtered = (List<RetrievedLocalManPage>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
