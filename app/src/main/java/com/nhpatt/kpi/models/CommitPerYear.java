package com.nhpatt.kpi.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Javier Gamarra
 */
public class CommitPerYear {

    private List<Commit> commits = new ArrayList<>();

    public CommitPerYear(List<Commit> commits) {
        this.commits = commits;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }
}
