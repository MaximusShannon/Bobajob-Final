package wit.bobajob.baj_maxshannon.Objects;

/**
 * Created by 20067975 on 2/20/2017.
 */

public class User {

    protected String userName;
    protected String userEmail;
    protected String userPassword;
    private String userId;
    private boolean signedForNewsletter;

    public User()
    {
        //Defualt required for calls to datasnapshot for firebase
    }

    public User(String userName, String userEmail, String userPassword, String userId, boolean signedForNewsletter) {

        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.signedForNewsletter = signedForNewsletter;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean isSignedForNewsletter() {
        return signedForNewsletter;
    }

    public void setSignedForNewsletter(boolean signedForNewsletter) {
        this.signedForNewsletter = signedForNewsletter;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userId=" + userId +
                '}';
    }

}
