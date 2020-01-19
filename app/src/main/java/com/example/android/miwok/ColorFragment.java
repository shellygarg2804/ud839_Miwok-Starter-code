package com.example.android.miwok;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.content.Context.AUDIO_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorFragment extends Fragment {
    MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener mcompletionListener = new MediaPlayer.OnCompletionListener() {
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

                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
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


    public ColorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /** TODO: Insert all the code from the NumberActivity’s onCreate() method after the setContentView method call */
        View rootView = inflater.inflate(R.layout.words_list, container, false);

        maudioManager = (AudioManager) getActivity().getSystemService(AUDIO_SERVICE);

        final ArrayList<Word> wordc = new ArrayList<Word>();
        wordc.add(new Word("Red", "weṭeṭṭi", R.drawable.color_red , R.raw.color_red));
        wordc.add(new Word("Green", "chokokki",R.drawable.color_green,R.raw.color_green));
        wordc.add(new Word("Brown", "ṭakaakki",R.drawable.color_brown, R.raw.color_brown));
        wordc.add(new Word("Gray", "ṭopoppi",R.drawable.color_gray, R.raw.color_gray));
        wordc.add(new Word("Black", "kululli",R.drawable.color_black, R.raw.color_black));
        wordc.add(new Word("White", "kelelli",R.drawable.color_white, R.raw.color_white));
        wordc.add(new Word("Dusty Yellow", "ṭopiisә",R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        wordc.add(new Word("Mustard Yellow", "chiwiiṭә",R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));


        wordAdaptor adaptor = new wordAdaptor(getActivity(), wordc, R.color.category_colors);

        final ListView listView = (ListView) rootView.findViewById(R.id.list);


        listView.setAdapter(adaptor);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Word word = wordc.get(position);
                releaseMediaPlayer();
                int result = maudioManager.requestAudioFocus(afChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback
                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getMaudioresourceid());
                    mMediaPlayer.start();
                    // Request audio focus for playback
                    mMediaPlayer.setOnCompletionListener(mcompletionListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
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
            // Abandon audio focus when playback complete
            maudioManager.abandonAudioFocus(afChangeListener);
        }
    }
}
