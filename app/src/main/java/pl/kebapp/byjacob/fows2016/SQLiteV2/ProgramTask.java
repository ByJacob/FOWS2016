package pl.kebapp.byjacob.fows2016.SQLiteV2;

/**
 * Created by ByJacob on 2016-09-29.
 *
 */

public class ProgramTask {
    private long mId;
    private int mDate;
    private int mStartH;
    private int mStartM;
    private int mEndH;
    private int mEndM;
    private String mTheme;
    private int mSpeakerID;
    private String mLang;

    public ProgramTask(long mId, int mDate, int mStartH, int mStartM, int mEndH, int mEndM, String mTheme, int mSpeakerID, String mLang) {
        this.mId = mId;
        this.mDate = mDate;
        this.mStartH = mStartH;
        this.mStartM = mStartM;
        this.mEndH = mEndH;
        this.mEndM = mEndM;
        this.mTheme = mTheme;
        this.mSpeakerID = mSpeakerID;
        this.mLang = mLang;
    }



    @Override
    public String toString() {
        return "ID:" + mId + "   Date:" + mDate + "   StartH:" + mStartH + "   StartM:" + mStartM +
                "   EndH:" + mEndH + "   EndM:" + mEndM + "   Theme:" + mTheme + "   SpeakerID:" +
                mSpeakerID + "   Lang:" + mLang;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public int getmDate() {
        return mDate;
    }

    public void setmDate(int mDate) {
        this.mDate = mDate;
    }

    public int getmStartH() {
        return mStartH;
    }

    public void setmStartH(int mStartH) {
        this.mStartH = mStartH;
    }

    public int getmStartM() {
        return mStartM;
    }

    public void setmStartM(int mStartM) {
        this.mStartM = mStartM;
    }

    public int getmEndH() {
        return mEndH;
    }

    public void setmEndH(int mEndH) {
        this.mEndH = mEndH;
    }

    public int getmEndM() {
        return mEndM;
    }

    public void setmEndM(int mEndM) {
        this.mEndM = mEndM;
    }

    public String getmTheme() {
        return mTheme;
    }

    public void setmTheme(String mTheme) {
        this.mTheme = mTheme;
    }

    public int getmSpeakerID() {
        return mSpeakerID;
    }

    public void setmSpeakerID(int mSpeakerID) {
        this.mSpeakerID = mSpeakerID;
    }

    public String getmLang() {
        return mLang;
    }

    public void setmLang(String mLang) {
        this.mLang = mLang;
    }
}
