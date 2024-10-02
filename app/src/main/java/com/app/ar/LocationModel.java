package com.app.ar;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.app.models.countries.CountryItemModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class
LocationModel implements Parcelable {
    String id;

    String name;
    Double lati;
    Double longi;
    int iconID;
    String category;
    Flags flag;
    String pavilionName;
    String capital;
    String description;
    Bitmap bitmap = null;
    String heading = " ";


    public static class Flags {
        private String png;

        public String getPng() {
            return png;
        }

        public void setPng(String png) {
            this.png = png;
        }
    }
    public String getFlagUrl() {
        return flag != null ? flag.getPng() : null;
    }

    public LocationModel() {
    }

    public LocationModel(String name, Double lati, Double longi, Flags flag, String capital){
        this.name = name;
        this.capital = capital;
        this.flag = flag;
        this.lati = lati;
        this.longi = longi;
    }

    public LocationModel(String name, String category, Double lati, Double longi) {
        this.name = name;
        this.category = category;
        this.lati = lati;
        this.longi = longi;
    }

    public LocationModel(String name, Double lati, Double longi, int iconID) {
        this.name = name;
        this.lati = lati;
        this.longi = longi;
        this.iconID = iconID;
    }

    public LocationModel(String name, Double lati, Double longi, int iconID, String heading) {
        this.name = name;
        this.lati = lati;
        this.longi = longi;
        this.iconID = iconID;
        this.heading = heading;
    }

    protected LocationModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            lati = null;
        } else {
            lati = in.readDouble();
        }
        if (in.readByte() == 0) {
            longi = null;
        } else {
            longi = in.readDouble();
        }
        iconID = in.readInt();
        category = in.readString();
        pavilionName = in.readString();
        description = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        if (lati == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(lati);
        }
        if (longi == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longi);
        }
        dest.writeInt(iconID);
        dest.writeString(category);
        dest.writeString(pavilionName);
        dest.writeString(description);
        dest.writeParcelable(bitmap, flags);

    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LocationModel> CREATOR = new Creator<LocationModel>() {
        @Override
        public LocationModel createFromParcel(Parcel in) {
            return new LocationModel(in);
        }

        @Override
        public LocationModel[] newArray(int size) {
            return new LocationModel[size];
        }
    };


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLati() {
        return lati;
    }

    public void setLati(Double lati) {
        this.lati = lati;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPavilionName() {
        return pavilionName;
    }

    public void setPavilionName(String pavilionName) {
        this.pavilionName = pavilionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }
}
