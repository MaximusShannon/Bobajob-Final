package wit.bobajob.baj_maxshannon.Objects;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by 20067975 on 2/20/2017.
 */

public  class Ad implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String adName;
    protected String adText;        //desc of add
    protected long phoneNumber;
    protected String customerIdRef;
    protected Boolean acceptEmails;
    protected Boolean acceptTerms;
    protected String category;
    protected String pictureTie;


    public Ad() {
    }


    public Ad(String adName, String adText, long phoneNumber, String customerIdRef, boolean acceptEmails, boolean acceptTerms, String category, String pictureTie) {

        this.adName = adName;
        this.adText = adText;
        this.phoneNumber = phoneNumber;
        this.customerIdRef = customerIdRef;
        this.acceptEmails = acceptEmails;
        this.acceptTerms = acceptTerms;
        this.category = category;
        this.pictureTie = pictureTie;

    }

    public String getAdName() {
        return adName;
    }

    public String getAdText() {
        return adText;
    }

    public String getPictureTie() {
        return pictureTie;
    }

    public String getCustomerIdRef() {
        return customerIdRef;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public Boolean getAcceptEmails() {
        return acceptEmails;
    }

    public Boolean getAcceptTerms() {
        return acceptTerms;
    }

    public String getCategory() {
        return category;
    }



    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("", adName);
        result.put("", adText);
        result.put("", phoneNumber);


        return result;

    }



    @Override
    public String toString() {
        return "Ad{" +
                "adName='" + adName + '\'' +
                ", adText='" + adText + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
