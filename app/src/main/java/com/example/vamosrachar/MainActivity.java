package com.example.vamosrachar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {
    EditText EditValorTotal, EditNumPessoas;
    TextView TextResultado;
    FloatingActionButton ShareButton, TocarButton;
    TextToSpeech ttsPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditValorTotal = (EditText) findViewById(R.id.editValorTotal);
        EditNumPessoas = (EditText) findViewById(R.id.editNumPessoas);
        TextResultado = (TextView) findViewById(R.id.textViewResultado);
        ShareButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonShare);
        TocarButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonTocar);

        EditValorTotal.addTextChangedListener(this);
        EditNumPessoas.addTextChangedListener(this);
        ShareButton.setOnClickListener(this);
        TocarButton.setOnClickListener(this);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 1122);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String valorTotal =  EditValorTotal.getText().toString();
        String numPessoas =  EditNumPessoas.getText().toString();

        if (!valorTotal.equals("") && !numPessoas.equals("")) {
            Double resultado = Double.parseDouble(valorTotal) / Double.parseDouble(numPessoas);
            DecimalFormat df = new DecimalFormat("#.00");
            String resultadoFormatado = df.format(resultado);
            TextResultado.setText("R$: " + resultadoFormatado);
        }
        else {
            TextResultado.setText("R$: 0,00");
        }
    }

    @Override
    public void onClick(View v) {
        String valorTotal =  EditValorTotal.getText().toString();
        String numPessoas =  EditNumPessoas.getText().toString();
        String resultado =  TextResultado.getText().toString();
        String strShareTTS = "A conta de R$" + valorTotal + " dividida para " + numPessoas + " pessoas deu " + resultado + "!";

        if (v == ShareButton) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, strShareTTS);
            startActivity(intent);
        }
        if (v == TocarButton) {
            if (ttsPlayer != null) {
                ttsPlayer.speak(strShareTTS, TextToSpeech.QUEUE_FLUSH, null, "ID1");
            }
        }
    }

    protected  void  onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1122) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                ttsPlayer = new TextToSpeech(this, this);
            }
            else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction((TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA));
                startActivity(installTTSIntent);
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(this, "TTS ativado!", Toast.LENGTH_LONG).show();
        }
        else if (status == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sem TTS Habilitado!", Toast.LENGTH_LONG).show();
        }
    }
}