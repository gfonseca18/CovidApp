package api;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by gfonseca on 13,Julho,2021
 */
public class ContinentData {
    @SerializedName("updated")
    private String updated;
    private String cases;
    private String deaths;
    private String recovered;
    private String population;

}
