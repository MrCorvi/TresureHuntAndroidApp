package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable{
    public int id;
    public boolean isPositionQuestion;
    public String question;
    public String answer;
    public boolean solved = false;

    public Step(int in_id, boolean ip, String q, String a){
        id = in_id;
        isPositionQuestion = ip;
        question = q;
        answer = a;
    }

    protected Step(Parcel in) {
        id = in.readInt();
        isPositionQuestion = in.readByte() != 0;
        question = in.readString();
        answer = in.readString();
        solved = in.readByte() != 0;
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeByte((byte) (isPositionQuestion ? 1 : 0));
        parcel.writeString(question);
        parcel.writeString(answer);
        parcel.writeByte((byte) (solved ? 1 : 0));
    }
}
