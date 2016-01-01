package com.sample.drawer;


public class Settings {
    public Settings() {
        showFavorite = false;
        deleteDoneDelay = 0;
        autoDeleteDone = false;
    }

    public void setShowFav(boolean newShowFav) {
        this.showFavorite = newShowFav;
    }

    public void setAutoDeleteDone(boolean autoDeleteDone) {
        this.autoDeleteDone = autoDeleteDone;
    }

    public void setDeleteDoneDelay(int deleteDoneDelay) {
        this.deleteDoneDelay = deleteDoneDelay;
    }

    public boolean getShowFav() {
        return showFavorite;
    }

    public boolean getAutoDeleteDone() {
        return autoDeleteDone;
    }

    public int getDeleteDoneDelay() {
        return deleteDoneDelay;
    }


    private boolean showFavorite, autoDeleteDone;
    private int deleteDoneDelay;
}
