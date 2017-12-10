package com.example.wissam.androiddataanalyser;

/**
 * Created by wissam on 11/10/17.
 */


public class Data {
    private Double  mNumberSolve;
    private Double  mTime;
    private Long    mId;


    public Data(){}
    public Data(Double numberSolve, Double time){
        this.mNumberSolve = numberSolve;
        this.mTime = time;
    }


    public void setId(long id) {
        this.mId = id;
    }
    public long getId() {
        return mId;
    }



    public void setNumberSolution(Double numberSolve) {
        this.mNumberSolve = numberSolve;
    }
    public Double getNumberSolution() {
        return mNumberSolve;
    }



    public void setTime(Double time) {
        this.mTime = time;
    }

    public Double getTime() {
        return mTime;
    }


    // Fonction qui sera utilisée par la classArrayAdapter dans la ListView
    @Override
    public String toString() {
        return mNumberSolve + " " + mTime + '\n';
    }

}

