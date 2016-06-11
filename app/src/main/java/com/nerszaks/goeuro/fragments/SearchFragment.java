package com.nerszaks.goeuro.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nerszaks.goeuro.MainActivity;
import com.nerszaks.goeuro.R;
import com.nerszaks.goeuro.adapters.LocationAutoCompleteAdapter;
import com.nerszaks.goeuro.content.Location;
import com.nerszaks.goeuro.utils.Utils;
import com.nerszaks.goeuro.views.AnimatedTextView;

import java.util.Calendar;

public class SearchFragment extends BaseFragment implements View.OnClickListener {

    private AutoCompleteTextView etDestFrom;
    private AutoCompleteTextView etDestTo;
    private LocationAutoCompleteAdapter destFromAdapter;
    private LocationAutoCompleteAdapter destToAdapter;
    private ImageButton btnDate;
    private Button btnSearch;

    public Calendar calendar = null;
    public Location locFrom = null;
    public Location locTo = null;

    private AnimatedTextView tvHeader;
    private TextView tvDate;
    private TextView lbDestFrom;
    private TextView lbDestTo;
    private TextView lbDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, null);

        tvHeader = (AnimatedTextView) view.findViewById(R.id.header);

        etDestFrom = (AutoCompleteTextView) view.findViewById(R.id.et_dest_from);
        etDestTo = (AutoCompleteTextView) view.findViewById(R.id.et_dest_to);
        btnDate = (ImageButton) view.findViewById(R.id.btn_date);
        btnSearch = (Button) view.findViewById(R.id.btn_search);
        tvDate = (TextView) view.findViewById(R.id.tv_date);

        lbDestFrom = (TextView) view.findViewById(R.id.lb_dest_from);
        lbDestTo = (TextView) view.findViewById(R.id.lb_dest_to);
        lbDate = (TextView) view.findViewById(R.id.lb_date);

        btnDate.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        tvDate.setOnClickListener(this);

        destFromAdapter = new LocationAutoCompleteAdapter();
        etDestFrom.setAdapter(destFromAdapter);
        etDestFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setLocFrom(destFromAdapter.getItem(position));
                Utils.hideKeyboard(etDestFrom);
                refreshBtnSearch();
            }
        });
        etDestFrom.addTextChangedListener(new AfterTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                setLocFrom(null);
                refreshBtnSearch();
            }
        });

        destToAdapter = new LocationAutoCompleteAdapter();
        etDestTo.setAdapter(destToAdapter);
        etDestTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setLocTo(destToAdapter.getItem(position));
                Utils.hideKeyboard(etDestTo);
                refreshBtnSearch();
            }
        });

        etDestTo.addTextChangedListener(new AfterTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                setLocTo(null);
                refreshBtnSearch();
            }
        });


        refreshBtnSearch();

        return view;
    }

    private void refreshBtnSearch() {
        btnSearch.setEnabled(locFrom != null && locTo != null && calendar != null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date:
            case R.id.btn_date:
                Utils.showDatePickerDialog((MainActivity) getActivity(), calendar != null ?
                        calendar.getTimeInMillis() : System.currentTimeMillis(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (calendar == null) {
                            calendar = Calendar.getInstance();
                            Utils.clearTimeCalendar(calendar);
                        }
                        hideView(lbDate);
                        Utils.updateCalendar(calendar, year, monthOfYear, dayOfMonth);
                        refreshDateField();
                        refreshBtnSearch();
                    }
                });
                break;
            case R.id.btn_search:
                Toast.makeText(getContext(), R.string.SearchNImplemented, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void refreshDateField() {
        tvDate.setText(calendar != null ? Utils.formatDateToDisplay(calendar.getTimeInMillis()) : "");
    }

    @Override
    public void onLocationUpdated() {
        super.onLocationUpdated();
        destFromAdapter.setCurrentLocation(((MainActivity) getActivity()).getCurrentLocation());
        destFromAdapter.refreshFilter();
    }

    public void setLocFrom(Location locFrom) {
        this.locFrom = locFrom;
        if (locFrom != null && lbDestFrom.getVisibility() == View.VISIBLE) {
            hideView(lbDestFrom);
        } else if (lbDestFrom.getVisibility() == View.INVISIBLE) {
            showView(lbDestFrom);
        }
    }

    public void setLocTo(Location locTo) {
        this.locTo = locTo;
        if (locTo != null && lbDestTo.getVisibility() == View.VISIBLE) {
            hideView(lbDestTo);
        } else if (lbDestTo.getVisibility() == View.INVISIBLE) {
            showView(lbDestTo);
        }
    }

    public void showView(View view) {
        lbDestFrom.clearAnimation();
        view.setVisibility(View.VISIBLE);
        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(500);
        view.startAnimation(animation1);
    }

    public void hideView(View view) {
        lbDestFrom.clearAnimation();
        view.setVisibility(View.INVISIBLE);
        AlphaAnimation animation1 = new AlphaAnimation(1.0f, 0.0f);
        animation1.setDuration(500);
        view.startAnimation(animation1);
    }
}
