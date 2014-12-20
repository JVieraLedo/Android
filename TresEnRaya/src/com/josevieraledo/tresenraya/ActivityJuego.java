package com.josevieraledo.tresenraya;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.josevieraledo.tresenraya.VistaJuego.TelefonoEstadoEscucha;
import com.josevieraledo.tresenraya.VistaJuego.Estado;


public class ActivityJuego extends Activity {

    public static final String EXTRA_START_PLAYER =
        "com.josevieraledo.tresenraya.ActivityJuego.EXTRA_START_PLAYER";

    private static final int MSG_COMPUTER_TURN = 1;
    private static final long COMPUTER_DELAY_MS = 500;

    private Handler mHandler = new Handler(new MyHandlerCallback());
    private Random mRnd = new Random();
    private VistaJuego mGameView;
    private TextView mInfoView;
    private Button mButtonNext;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        
        setContentView(R.layout.layout_normal);

        mGameView = (VistaJuego) findViewById(R.id.vista_juego);
        mInfoView = (TextView) findViewById(R.id.informacion_turno);
        mButtonNext = (Button) findViewById(R.id.siguiente_turno);

        mGameView.setFocusable(true);
        mGameView.setFocusableInTouchMode(true);
        mGameView.setTelefonoAccion(new MyCellListener());

        mButtonNext.setOnClickListener(new MyButtonListener());
    }

    @Override
    protected void onResume() {
        super.onResume();

        Estado jugador = mGameView.getJugadorActual();
        if (jugador == Estado.DESCONOCIDO) {
        	jugador = Estado.fromInt(getIntent().getIntExtra(EXTRA_START_PLAYER, 1));
            if (!checkGameFinished(jugador)) {
                selectTurno(jugador);
            }
        }
        if (jugador == Estado.JUGADOR2) {
            mHandler.sendEmptyMessageDelayed(MSG_COMPUTER_TURN, COMPUTER_DELAY_MS);
        }
        if (jugador == Estado.GANAR) {
            setWinState(mGameView.getWinner());
        }
    }


    private Estado selectTurno(Estado jugador) {
        mGameView.setJugadorActual(jugador);
        mButtonNext.setEnabled(false);

        if (jugador == Estado.JUGADOR1) {
            mInfoView.setText(R.string.jugador1_turno);
            mGameView.setEnabled(true);

        } else if (jugador == Estado.JUGADOR2) {
            mInfoView.setText(R.string.jugador2_turno);
            mGameView.setEnabled(false);
        }

        return jugador;
    }

    private class MyCellListener implements TelefonoEstadoEscucha {
        public void onAccionTelefono() {
            if (mGameView.getJugadorActual() == Estado.JUGADOR1) {
                int cell = mGameView.getSelection();
                mButtonNext.setEnabled(cell >= 0);
            }
        }
    }

    private class MyButtonListener implements OnClickListener {

        public void onClick(View v) {
            Estado jugador = mGameView.getJugadorActual();

            if (jugador == Estado.GANAR) {
                ActivityJuego.this.finish();

            } else if (jugador == Estado.JUGADOR1) {
                int telefono = mGameView.getSelection();
                if (telefono >= 0) {
                    mGameView.stopBlink();
                    mGameView.setTelefono(telefono, jugador);
                    finishTurn();
                }
            }
        }
    }

    private class MyHandlerCallback implements Callback {
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_COMPUTER_TURN) {

                // Pick a non-used cell at random. That's about all the AI you need for this game.
                Estado[] data = mGameView.getData();
                int used = 0;
                while (used != 0x1F) {
                    int index = mRnd.nextInt(9);
                    if (((used >> index) & 1) == 0) {
                        used |= 1 << index;
                        if (data[index] == Estado.VACIO) {
                            mGameView.setTelefono(index, mGameView.getJugadorActual());
                            break;
                        }
                    }
                }

                finishTurn();
                return true;
            }
            return false;
        }
    }

    private Estado getOtherPlayer(Estado jugador) {
        return jugador == Estado.JUGADOR1 ? Estado.JUGADOR2 : Estado.JUGADOR1;
    }

    private void finishTurn() {
        Estado jugador = mGameView.getJugadorActual();
        if (!checkGameFinished(jugador)) {
            jugador = selectTurno(getOtherPlayer(jugador));
            if (jugador == Estado.JUGADOR2) {
                mHandler.sendEmptyMessageDelayed(MSG_COMPUTER_TURN, COMPUTER_DELAY_MS);
            }
        }
    }

    public boolean checkGameFinished(Estado jugador) {
        Estado[] data = mGameView.getData();
        boolean full = true;

        int col = -1;
        int row = -1;
        int diag = -1;

        // check rows
        for (int j = 0, k = 0; j < 3; j++, k += 3) {
            if (data[k] != Estado.VACIO && data[k] == data[k+1] && data[k] == data[k+2]) {
                row = j;
            }
            if (full && (data[k] == Estado.VACIO ||
                         data[k+1] == Estado.VACIO ||
                         data[k+2] == Estado.VACIO)) {
                full = false;
            }
        }

        // check columns
        for (int i = 0; i < 3; i++) {
            if (data[i] != Estado.VACIO && data[i] == data[i+3] && data[i] == data[i+6]) {
                col = i;
            }
        }

        // check diagonals
        if (data[0] != Estado.VACIO && data[0] == data[1+3] && data[0] == data[2+6]) {
            diag = 0;
        } else  if (data[2] != Estado.VACIO && data[2] == data[1+3] && data[2] == data[0+6]) {
            diag = 1;
        }

        if (col != -1 || row != -1 || diag != -1) {
            setFinished(jugador, col, row, diag);
            return true;
        }

        // if we get here, there's no winner but the board is full.
        if (full) {
            setFinished(Estado.VACIO, -1, -1, -1);
            return true;
        }
        return false;
    }

    private void setFinished(Estado jugador, int columna, int fila, int diagonal) {

        mGameView.setJugadorActual(Estado.GANAR);
        mGameView.setWinner(jugador);
        mGameView.setEnabled(false);
        mGameView.setFinished(columna, fila, diagonal);

        setWinState(jugador);
    }

    private void setWinState(Estado jugador) {
        mButtonNext.setEnabled(true);
        mButtonNext.setText("EMPEZAR");

        String text;

        if (jugador == Estado.VACIO) {
            text = getString(R.string.empate);
        } else if (jugador == Estado.JUGADOR1) {
            text = getString(R.string.jugador1_gana);
        } else {
            text = getString(R.string.jugador2_gana);
        }
        mInfoView.setText(text);
    }
}
