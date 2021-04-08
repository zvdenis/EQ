package com.SuperClub.EQ.Data;

import com.google.gson.annotations.SerializedName;

public class QueueInfo {

    @SerializedName("id")
    public String id;

    @SerializedName("eqOwnerId")
    public String ownerId;

    @SerializedName("eqAverageWaitingTime")
    public float averageWaitingTime;

    @SerializedName("eqCurrentUser")
    public String currentUser;

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
    public String usersBeforeMe;

    @SerializedName("expectedTime")
    public String expectedTime;
}
