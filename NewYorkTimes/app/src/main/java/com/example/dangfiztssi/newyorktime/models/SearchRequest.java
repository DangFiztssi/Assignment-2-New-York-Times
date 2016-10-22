package com.example.dangfiztssi.newyorktime.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DangF on 10/20/16.
 */

public class SearchRequest implements Parcelable {
    private static final String[] sortOrder = {"newest", "oldest"};
    private static final String[] deskValue = {"\"Arts\"", "\"Fashion & Style\"", "\"Sports\"", "\"Travel\""};
    private int page = 0;
    private int indexOrder = 0;
    private String query = "";
    private List<Integer> deskValues = new ArrayList<>();
    private long startDate = 1451581200;

    public SearchRequest() {
    }

    public SearchRequest(Parcel in) {
        page = in.readInt();
        indexOrder = in.readInt();
        query = in.readString();
        startDate = in.readLong();
    }


    public static final Creator<SearchRequest> CREATOR = new Creator<SearchRequest>() {
        @Override
        public SearchRequest createFromParcel(Parcel in) {
            return new SearchRequest(in);
        }

        @Override
        public SearchRequest[] newArray(int size) {
            return new SearchRequest[size];
        }
    };

    public long getStartDate() {
        return startDate;
    }

    public List<Integer> getDeskValues() {
        return deskValues;
    }

    public void setDeskValues(List<Integer> d) {
        deskValues.clear();
        deskValues.addAll(d);
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return indexOrder + " - " + deskValues;
    }

    public void setIndexOrder(int indexOrder) {
        this.indexOrder = indexOrder;
    }

    public int getIndexOrder() {
        return indexOrder;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void nextPage() {
        page += 1;
    }

    public void resetPage() {
        page = 0;
    }

    public void addValueDesk(int pos) {
        if (!deskValues.contains(pos))
            deskValues.add(pos);
    }

    public void delValueDesk(int pos) {
        if (deskValues.contains(pos))
            deskValues.remove(pos);
    }

    public String convertFromDate(long d) {
        Date da = new Date(d * 1000);
        return (da.getYear() + 1900) + "" + String.format("%02d", da.getMonth() + 1) + ""
                + String.format("%02d", da.getDate());
    }

    public Map<String, String> toQueryMay() {
        Map<String, String> options = new HashMap<>();

        options.put("begin_date", convertFromDate(startDate));
        options.put("sort", sortOrder[indexOrder]);

        String tmp = "";
        for (int i : deskValues) {
            tmp += deskValue[i];
            if (i != deskValues.size() - 1)
                tmp += ",";
        }
        if (tmp.contains(","))
            tmp = tmp.substring(0, tmp.length() - 1);

        if (!tmp.isEmpty())
            options.put("fq", "news_desk:(" + tmp + ")");

        if (!query.equalsIgnoreCase(""))
            options.put("q", query);

        options.put("page", String.valueOf(page));

        return options;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeInt(indexOrder);
        dest.writeString(query);
        dest.writeLong(startDate);
    }
}
