package com.SuperClub.EQ.Data;

import com.SuperClub.EQ.Application.Configs;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.util.Date;

public class QueueInfo {

    @SerializedName("id")
    public String id;

    @SerializedName("eqOwnerId")
    public String ownerId;

    @SerializedName("eqAverageWaitingTime")
    public float averageWaitingTime;

    @SerializedName("eqCurrentUser")
    public int currentUser;

    @SerializedName("eqDateStart")
    public String dateStart;

    @SerializedName("eqDateEnd")
    public String dateEnd;

    @SerializedName("eqType")
    public String type;

    @SerializedName("eqMaxUsers")
    public int maxUsers;

    @SerializedName("eqTitle")
    public String title;

    @SerializedName("eqDescription")
    public String description;

    @SerializedName("code")
    public String code;

    @SerializedName("usersBeforeMe")
    public int usersBeforeMe;

    @SerializedName("expectedTime")
    public String expectedTime;


    public Date getStartDate() {
        try {
            return Configs.fullFormat.parse(dateStart);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date getEndDate() {
        try {
            return Configs.fullFormat.parse(dateEnd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getStartDateString() {
        return Configs.dateFormat.format(getStartDate());
    }

    public String getEndDateString() {
        return Configs.dateFormat.format(getEndDate());
    }

    public String getStartTimeString() {
        return Configs.timeFormat.format(getStartDate());
    }

    public String getEndTimeString() {
        return Configs.timeFormat.format(getEndDate());
    }

    public String getExpectedTime() {
        Double number = Double.parseDouble(expectedTime);
        int a = (int) Math.round(number);
        return Integer.toString(a);
    }

    public int getLiveUserCount() {
        return usersBeforeMe - currentUser;
    }


}
