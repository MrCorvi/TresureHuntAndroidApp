package com.example.demo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GamePK implements Serializable {
    @Column(name = "gameID", nullable = false)
    protected Integer gameID; // int isn't nullable
    @Column(name = "step", nullable=false)
    protected Integer step;

    public GamePK() {}

    public GamePK(Integer gameID, Integer step) {
        this.gameID=gameID;
        this.step=step;
    }
    public Integer getGameId(){return gameID;}
    public Integer getStep(){return step;}
    public void setGameId(Integer gameID){this.gameID=gameID;}
    public void setStep(Integer step){this.step=step;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GamePK)) return false;
        GamePK that = (GamePK) o;
        return Objects.equals(getGameId(), that.getGameId()) &&
                Objects.equals(getStep(), that.getStep());
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(getGameId(), getStep());
    }

}