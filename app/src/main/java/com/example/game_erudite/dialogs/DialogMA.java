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

import com.example.game_erudite.activity.MainMenu;
import com.example.game_erudite.R;


public class DialogMA extends DialogFragment implements View.OnClickListener, SoundPool.OnLoadCompleteListener {

    private Button btnOK,btnNo;

    private SoundPool soundPool;
    private int spClicButton;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initSoundPool();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ma, null);
        builder.setView(view)
                .setCancelable(false);

        btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(this);

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
            case R.id.btnOK:
                soundPool.play(spClicButton,1,1,1,0,1);
                ((MainMenu)getActivity()).finish();
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
