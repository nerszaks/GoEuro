package com.nerszaks.goeuro.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.nerszaks.goeuro.GoEuroApp;
import com.nerszaks.goeuro.R;
import com.nerszaks.goeuro.api.ApiMethods;
import com.nerszaks.goeuro.content.Location;
import com.nerszaks.goeuro.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LocationAutoCompleteAdapter extends BaseAdapter implements Filterable {

    public List<Location> productOrCategoryObjs = new ArrayList<>();
    public List<Location> visibleLocations = new ArrayList<>();

    public LayoutInflater inflater;
    private android.location.Location currentLocation;

    public LocationAutoCompleteAdapter() {
        inflater = LayoutInflater.from(GoEuroApp.getInstance());
    }

    @Override
    public int getCount() {
        return visibleLocations.size();
    }

    @Override
    public Location getItem(int position) {
        return visibleLocations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setData(List<Location> productOrCategoryObjs) {
        this.productOrCategoryObjs = productOrCategoryObjs;
        notifyDataSetChanged();
    }

    public void setCurrentLocation(android.location.Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public class ViewHolder {
        public TextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_location, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Location item = getItem(position);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(item.name);
        stringBuilder.append(" (");
        stringBuilder.append(item.country);
        stringBuilder.append(")");
        holder.name.setText(stringBuilder.toString());
        return view;
    }

    private String prevFilterRqText = null;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null && (prevFilterRqText == null || !prevFilterRqText.equals(constraint.toString()))) {
                    String filterText = constraint.toString();
                    loadLocations(filterText);
                    prevFilterRqText = filterText;
                }

                ArrayList<Location> resultList = new ArrayList<>();
                resultList.addAll(productOrCategoryObjs);
                FilterResults filterResults = new FilterResults();
                filterResults.count = resultList.size();
                filterResults.values = resultList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count > 0) {
                    visibleLocations = (ArrayList<Location>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    private void loadLocations(String filterText) {
        if (!Utils.isOnline()) {
            return;
        }
        String displayLanguage = Locale.getDefault().getLanguage();
        Log.d("loadLocations", displayLanguage + " " + filterText);
        ApiMethods service = GoEuroApp.getInstance().getService();
        try {
            service.listLocations(displayLanguage, filterText)
                    .map(new Func1<List<Location>, List<Location>>() {
                        @Override
                        public List<Location> call(List<Location> locations) {
                            if (currentLocation != null) {
                                for (Location location : locations) {
                                    location.distance = currentLocation.distanceTo(location.geoPosition.getLocationObj());
                                }
                                Collections.sort(locations, new Comparator<Location>() {
                                    @Override
                                    public int compare(Location lhs, Location rhs) {
                                        if (lhs.distance < rhs.distance) {
                                            return -1;
                                        } else if (lhs.distance > rhs.distance) {
                                            return 1;
                                        } else {
                                            return 0;
                                        }
                                    }
                                });
                            }
                            return locations;
                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                        }
                    })
                    .subscribe(new Action1<List<Location>>() {
                        @Override
                        public void call(List<Location> locations) {
                            setData(locations);
                            refreshFilter();
                        }
                    });
        } catch (Throwable throwable) {

        }
    }

    public void refreshFilter() {
        getFilter().filter(prevFilterRqText != null && prevFilterRqText.length() > 1 ? prevFilterRqText : null);
    }

}
