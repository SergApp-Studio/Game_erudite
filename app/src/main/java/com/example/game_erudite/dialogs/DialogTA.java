package com.example.game_erudite.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.example.game_erudite.R;
import com.example.game_erudite.activity.GamePage;

public class DialogTA extends DialogFragment implements View.OnClickListener, SoundPool.OnLoadCompleteListener {

    private Button btnYes, btnNo;

    private SoundPool soundPool;
    private int spClicButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initSoundPool();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ta, null);
        builder.setView(view)
                .setCancelable(true);

        btnYes = view.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(this);

        btnNo = view.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(this);


        return builder.create();
    }


    private void initSoundPool(){
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        spClicButton = soundPool.load(getContext(),R.raw.clic_button,1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnYes:
                soundPool.play(spClicButton,1,1,1,0,1);
                // сохранялка и отправление результатов в активити рекордов
                ((GamePage)getActivity()).finish();
                break;
            case R.id.btnNo:
                soundPool.play(spClicButton,1,1,1,0,1);
                dismiss();
                break;
        }
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
