package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class family extends AppCompatActivity {
    MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener mcompletionListener =  new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };
    AudioManager maudioManager;
    AudioManager.OnAudioFocusChangeListener afChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // Permanent loss of audio focus
                        // Pause playback immediately
                        releaseMediaPlayer();

                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT|| focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                        // Pause playback
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Your app has been granted audio focus again
                        // Raise volume to normal, restart playback if necessary
                        mMediaPlayer.start();
                    }
                }
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_list);
        maudioManager= (AudioManager) getSystemService(AUDIO_SERVICE);
        final ArrayList<Word> wordf = new ArrayList<Word>();
        wordf.add(new Word("Father", "әpә", R.drawable.family_father, R.raw.family_father));
        wordf.add(new Word("Mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
        wordf.add(new Word("Son", "angsi", R.drawable.family_son, R.raw.family_son));
        wordf.add(new Word("Daughter", "ṭune", R.drawable.family_daughter, R.raw.family_daughter));
        wordf.add(new Word("Older Brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        wordf.add(new Word("Younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        wordf.add(new Word("Older Sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        wordf.add(new Word("Younger Sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        wordf.add(new Word("Grandmother", "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        wordf.add(new Word("Grandfather", "pappa", R.drawable.family_grandfather, R.raw.family_grandfather));

        wordAdaptor adaptor = new wordAdaptor(this, wordf, R.color.category_family);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adaptor);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Word word = wordf.get(position);
                releaseMediaPlayer();
                int result = maudioManager.requestAudioFocus(afChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback
                    mMediaPlayer = MediaPlayer.create(family.this, word.getMaudioresourceid());
                    mMediaPlayer.start();
                    // Request audio focus for playback
                    mMediaPlayer.setOnCompletionListener(mcompletionListener);
                }

            }
        });
    }
        protected void onStop() {  //stops playing audio files when user switch to another app in middle of the audio.
            super.onStop();
            releaseMediaPlayer();
        }
        private void releaseMediaPlayer(){
            // If the media player is not null, then it may be currently playing a sound.
            if (mMediaPlayer != null) {
                // Regardless of the current state of the media player, release its resources
                // because we no longer need it.
                mMediaPlayer.release();

                // Set the media player back to null. For our code, we've decided that
                // setting the media player to null is an easy way to tell that the media player
                // is not configured to play an audio file at the moment.
                mMediaPlayer = null;
                maudioManager.abandonAudioFocus(afChangeListener);
            }
        }

}
