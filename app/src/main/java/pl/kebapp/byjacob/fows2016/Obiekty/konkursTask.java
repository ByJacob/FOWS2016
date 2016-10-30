package pl.kebapp.byjacob.fows2016.Obiekty;

/**
 * Created by ByJacob on 2016-10-11.
 */

public class konkursTask {
    private int mID;
    private String mPYTANIE;
    private String mODP1;
    private String mODP2;
    private String mODP3;
    private String mODP4;
    private int mNRodp = 0;

    public konkursTask(int mID, String mPYTANIE, String mODP1, String mODP2, String mODP3, String mODP4) {
        this.mID = mID;
        this.mPYTANIE = mPYTANIE;
        this.mODP1 = mODP1;
        this.mODP2 = mODP2;
        this.mODP3 = mODP3;
        this.mODP4 = mODP4;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmPYTANIE() {
        return mPYTANIE;
    }

    public void setmPYTANIE(String mPYTANIE) {
        this.mPYTANIE = mPYTANIE;
    }

    public String getmODP1() {
        return mODP1;
    }

    public void setmODP1(String mODP1) {
        this.mODP1 = mODP1;
    }

    public String getmODP2() {
        return mODP2;
    }

    public void setmODP2(String mODP2) {
        this.mODP2 = mODP2;
    }

    public String getmODP3() {
        return mODP3;
    }

    public void setmODP3(String mODP3) {
        this.mODP3 = mODP3;
    }

    public String getmODP4() {
        return mODP4;
    }

    public void setmODP4(String mODP4) {
        this.mODP4 = mODP4;
    }

    public String getmNRodp() {
        return String.valueOf(mNRodp);
    }

    public void setmNRodp(int mNRodp) {
        this.mNRodp = mNRodp;
    }
}
