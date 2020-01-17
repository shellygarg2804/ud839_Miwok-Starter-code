package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class phrases extends AppCompatActivity {
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
        final ArrayList<Word> wordp = new ArrayList<Word>();
        wordp.add(new Word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        wordp.add(new Word("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        wordp.add(new Word("My name is..", "oyaaset..", R.raw.phrase_my_name_is));
        wordp.add(new Word("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
        wordp.add(new Word("I’m feeling good", "kuchi achit", R.raw.phrase_im_feeling_good));
        wordp.add(new Word("Are you coming? ", "әәnәs'aa?", R.raw.phrase_are_you_coming));
        wordp.add(new Word("Yes, I’m coming", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        wordp.add(new Word("I’m coming. ", "әәnәm", R.raw.phrase_im_coming));
        wordp.add(new Word("Lets Go.", "yoowutis", R.raw.phrase_lets_go));
        wordp.add(new Word("Come here.", "әnni'nem", R.raw.phrase_come_here));


        wordAdaptor adaptor = new wordAdaptor(this, wordp, R.color.category_phrases);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adaptor);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Word word = wordp.get(position);
                releaseMediaPlayer();
                int result = maudioManager.requestAudioFocus(afChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback
                    mMediaPlayer = MediaPlayer.create(phrases.this, word.getMaudioresourceid());
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

    private void releaseMediaPlayer() {
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
