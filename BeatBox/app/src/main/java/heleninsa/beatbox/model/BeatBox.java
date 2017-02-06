package heleninsa.beatbox.model;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heleninsa on 2017/1/29.
 */

public class BeatBox {

    private final static String TAG = "BeatBox";

    private final static String SOUNDS_FOLDER = "sample_sounds";
    private final static int MAX_SOUNDS = 5;

    private AssetManager mAssetManager;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;

    public BeatBox(Context context) {
        mAssetManager = context.getAssets();
        mSoundPool = new SoundPool.Builder().
                        setMaxStreams(MAX_SOUNDS).
                        setAudioAttributes(
                                new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
                        ). build();
        loadSounds();
    }

    private void loadSounds() {
        String[] soundsName;

        try {
            soundsName = mAssetManager.list(SOUNDS_FOLDER);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        for (String fileName : soundsName) {
            String assertPath = SOUNDS_FOLDER + "/" + fileName;
            Log.e(TAG, "Sounds P  :  " + fileName);
            Sound sound = new Sound(assertPath);
            mSounds.add(sound);
            try {
                load(sound);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Sound> getSounds() {
        return mSounds;
    }

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssetManager.openFd(sound.getAssertPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if(soundId == null) {
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }


    /**
     * 释放资源
     */
    public void release() {
        mSoundPool.release();
    }
}
