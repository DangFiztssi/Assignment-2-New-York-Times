package com.example.dangfiztssi.newyorktime.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dangfiztssi.newyorktime.R;
import com.example.dangfiztssi.newyorktime.models.SearchRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DangF on 10/22/16.
 */

public class FilterSearchDialogFragment extends DialogFragment {

    @BindView(R.id.tvDateStart)
    TextView tvDate;

    @BindView(R.id.spOrder)
    Spinner spOrder;

    @BindView(R.id.cbArts)
    CheckBox cbArts;

    @BindView(R.id.cbTravel)
    CheckBox cbTravel;

    @BindView(R.id.cbFashion)
    CheckBox cbFashion;

    @BindView(R.id.cbSport)
    CheckBox cbSport;

    SearchRequest request;

    long dateTmp = 0;

    public interface FilterListener{
        void onFinishFilter(SearchRequest result);
    }

    public FilterSearchDialogFragment() {
        //Require but not pass argument in here, via newInstance
    }

    public static FilterSearchDialogFragment newInstance(SearchRequest request) {

        Bundle args = new Bundle();
        //TODO: put request into args
        args.putParcelable("data", request);

        FilterSearchDialogFragment fragment = new FilterSearchDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view  = View.inflate(getContext(),R.layout.dialog_fragment_sort_filter,null);
        ButterKnife.bind(this, view);
        builder.setView(view);

        request = getArguments().getParcelable("data");

        dateTmp = request.getStartDate();
        setupStartDate(request.getStartDate());
        setupSpinner(request.getIndexOrder());
        setupStartDate(request.getStartDate());
        setupCheckBox(request.getDeskValues());

        builder.setTitle("Setting Filter");


        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setRequest();
                FilterListener listener = (FilterListener) getActivity();
                listener.onFinishFilter(request);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        builder.setCancelable(false);

        return builder.create();
    }

    void setupSpinner(int pos){
        spOrder.setSelection(pos);
    }

    private void setupStartDate(long d){
        tvDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(d * 1000)));
    }

    private void showDatePicker(){
        Date date = new Date(dateTmp * 1000);
        Log.e("date date date", date.getYear() + "");
        DatePickerDialog dia = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date d = new Date(year-1900, month, dayOfMonth);
                dateTmp = d.getTime() / 1000;
                setupStartDate(dateTmp);
            }
        }, date.getYear() + 1900, date.getMonth(), date.getDate());

        dia.show();
    }

    private void setupCheckBox(List<Integer> checkList){
        for(int i : checkList){
            switch (i){
                case 0:
                    cbArts.setChecked(true);
                    break;
                case 1:
                    cbFashion.setChecked(true);
                    break;
                case 2:
                    cbSport.setChecked(true);
                    break;
                case 3:
                    cbTravel.setChecked(true);
                    break;
            }
        }
    }

    private void setRequest(){
        request.setStartDate(dateTmp);

        request.setIndexOrder(spOrder.getSelectedItemPosition());

        deskValue(cbArts, 0);
        deskValue(cbFashion, 1);
        deskValue(cbSport, 2);
        deskValue(cbTravel, 3);
    }

    private void deskValue(CheckBox cb, int pos){
        if(cb.isChecked())
            request.addValueDesk(pos);
        else
            request.delValueDesk(pos);
    }

}
