package pl.kebapp.byjacob.fows2016.SQLiteV2;

/**
 * Created by ByJacob on 2016-10-04.
 */

public class PrelegenciTask {
    private long mID;
    private String mNAME;
    private String mINFO;
    private String mURLPICTURE;
    private String mCOMPANY;

    public PrelegenciTask(long mID, String mNAME, String mINFO, String mURLPICTURE,String
            mCOMPANY) {
        this.mID = mID;
        this.mNAME = mNAME;
        this.mINFO = mINFO;
        this.mURLPICTURE = mURLPICTURE;
        this.mCOMPANY = mCOMPANY;
    }

    @Override
    public String toString() {
        return "ID:" + mID + "   NAME:" + mNAME + "   INFO:" + mINFO + "   URLPICTURE:" +
                mURLPICTURE + "   COMPANY:" + mCOMPANY;
    }

    public long getmID() {
        return mID;
    }

    public void setmID(long mID) {
        this.mID = mID;
    }

    public String getmNAME() {
        return mNAME;
    }

    public void setmNAME(String mNAME) {
        this.mNAME = mNAME;
    }

    public String getmINFO() {
        return mINFO;
    }

    public void setmINFO(String mINFO) {
        this.mINFO = mINFO;
    }

    public String getmURLPICTURE() {
        return mURLPICTURE;
    }

    public void setmURLPICTURE(String mURLPICTURE) {
        this.mURLPICTURE = mURLPICTURE;
    }

    public String getmCOMPANY() {
        return mCOMPANY;
    }

    public void setmCOMPANY(String mCOMPANY) {
        this.mCOMPANY = mCOMPANY;
    }
}
