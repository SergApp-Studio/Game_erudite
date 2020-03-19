package com.example.game_erudite.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.game_erudite.R;

import java.io.IOException;

public  class Dialog_GameOver extends DialogFragment implements SoundPool.OnLoadCompleteListener{


    private  TextView tvScore_Dialog;
    private Button btnOK;
    private   String score;

    private SoundPool soundPool;
    private int spClicButton;




    @Override
    public  Dialog onCreateDialog (Bundle savedInstanceState) {

        initSoundPool();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_gameover,null);
        builder.setView(view)
                .setCancelable(true);
        tvScore_Dialog = view.findViewById(R.id.tvScore_Dialog);
        tvScore_Dialog.setText(tvScore_Dialog.getText() +"     " + score);
        btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(spClicButton,1,1,1,0,1);
                getActivity().finish();
            }
        });
        return builder.create();
    }

    private void initSoundPool(){
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        spClicButton = soundPool.load(getContext(),R.raw.clic_button,1);
    }

    public void setScore(String score){

        this.score=score;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        soundPool.release();
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }
}
