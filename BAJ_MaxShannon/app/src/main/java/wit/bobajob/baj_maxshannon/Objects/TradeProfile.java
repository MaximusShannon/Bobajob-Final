package wit.bobajob.baj_maxshannon.Objects;

import java.io.Serializable;

/**
 * Created by Maximus on 4/26/2017.
 */

public class TradeProfile implements Serializable {

    private static final long serialVersionUID = 2L;

    private String tradeName;
    private String tradeEmail;
    private long tradeNumber;
    private String tradeLocation;
    private String tradeExperience;
    private String tradeCounty;
    private boolean availDays, availNights, unskilledWorker, skilledWorker;
    private String userId;
    private String iconTie;

    public TradeProfile()
    {

    }

    public TradeProfile(String tradeName, String tradeEmail, long tradeNumber, String tradeLocation, String tradeExperience, String tradeCounty, boolean availDays, boolean availNights, boolean unskilledWorker, boolean skilledWorker, String userId, String iconTie) {
        this.tradeName = tradeName;
        this.tradeEmail = tradeEmail;
        this.tradeNumber = tradeNumber;
        this.tradeLocation = tradeLocation;
        this.tradeExperience = tradeExperience;
        this.tradeCounty = tradeCounty;
        this.availDays = availDays;
        this.availNights = availNights;
        this.unskilledWorker = unskilledWorker;
        this.skilledWorker = skilledWorker;
        this.userId = userId;
        this.iconTie = iconTie;
    }

    public String getTradeName() {
        return tradeName;
    }

    public String getTradeEmail() {
        return tradeEmail;
    }

    public long getTradeNumber() {
        return tradeNumber;
    }

    public String getTradeLocation() {
        return tradeLocation;
    }

    public String getTradeExperience() {
        return tradeExperience;
    }

    public String getTradeCounty() {
        return tradeCounty;
    }

    public boolean isAvailDays() {
        return availDays;
    }

    public boolean isAvailNights() {
        return availNights;
    }

    public boolean isUnskilledWorker() {
        return unskilledWorker;
    }

    public boolean isSkilledWorker() {
        return skilledWorker;
    }


    public String getUserId() {
        return userId;
    }

    public String getIconTie() {
        return iconTie;
    }



}




