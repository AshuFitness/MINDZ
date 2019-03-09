
package com.mindzglobal.www.mindz.Model.blocklist;

import java.util.List;
import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class BlocklistPojo {

    @SerializedName("UBlockList")
    private List<UBlockList> mUBlockList;

    public List<UBlockList> getUBlockList() {
        return mUBlockList;
    }

    public void setUBlockList(List<UBlockList> uBlockList) {
        mUBlockList = uBlockList;
    }

}
