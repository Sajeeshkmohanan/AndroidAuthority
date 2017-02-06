package heleninsa.beatbox.model;

/**
 * Created by heleninsa on 2017/1/29.
 */

public class Sound {
    private String mAssertPath;
    private String mName;
    private Integer mSoundId;


    public Sound(String assertPath) {
        mAssertPath = assertPath;
        //Delete the front-name
        String[] components = assertPath.split("/");
        String fileName = components[components.length - 1];
        //Delete the .wav
        mName = fileName.replace(".wav", "");
    }

    public String getAssertPath() {
        return mAssertPath;
    }

    public String getName() {
        return mName;
    }

    public Integer getSoundId() {
        return mSoundId;
    }

    public void setSoundId(Integer soundId) {
        mSoundId = soundId;
    }
}
