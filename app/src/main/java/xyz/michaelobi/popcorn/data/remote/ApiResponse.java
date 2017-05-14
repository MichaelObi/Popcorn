package xyz.michaelobi.popcorn.data.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Popcorn
 * Michael Obi
 * 13 05 2017 9:19 AM
 */

public class ApiResponse<T> {

    @SerializedName("results")
    private List<T> results;

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
