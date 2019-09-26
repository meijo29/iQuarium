package com.example.iquarium;

import java.util.ArrayList;

public class Fishes {


    public String Name, topDwell, midDwell, botDwell, res;
    public Float pHMax;
    public Float phMin;
    public Integer tankMin;
    public Integer tempMin;
    public Integer tempMax;
    public Integer size;
    public Integer aqID;
    public Integer aqGal;
    public Integer qty;
    public Integer fishID;

    public String Image;
    public ArrayList<String> players=new ArrayList<>();

    public Fishes(Integer fishID, String Name,Integer size, Float pHmin, Float pHMax, Integer tempMin, Integer tempMax,Integer tankMin, String topDwell, String midDwell, String botDwell, Integer aqID, Integer aqGal, Integer qty, String res)
    {
        this.fishID = fishID;
        this.Name=Name;
        this.phMin = pHmin;
        this.pHMax = pHMax;
        this.tankMin = tankMin;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.size = size;
        this.topDwell = topDwell;
        this.midDwell = midDwell;
        this.botDwell = botDwell;
        this.aqID = aqID;
        this.aqGal = aqGal;
        this.qty = qty;
        this.res = res;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return Name;
    }

    public Float getpHMin()
    {
        return phMin;
    }

    public Float getpHMax()
    {
        return  pHMax;
    }

    public Integer getTankMin()
    {
        return tankMin;
    }

    public Integer getTempMin()
    {
        return tempMin;
    }

    public Integer getTempMax()
    {
        return tempMax;
    }

    public Integer getSize()
    {
        return size;
    }

    public String getTopDwell()
    {
        return topDwell;
    }

    public String getMidDwell()
    {
        return midDwell;
    }

    public String getBotDwell()
    {
        return botDwell;
    }

    public Integer getAqID()
    {
        return  aqID;
    }

}
