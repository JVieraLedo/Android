package com.josevieraledo.tresenraya;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Handler.Callback;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

//-----------------------------------------------

public class VistaJuego extends View {

    public static final long FPS_MS = 1000/2;

    public enum Estado {
        DESCONOCIDO(-3),
        GANAR(-2),
        VACIO(0),
        JUGADOR1(1),
        JUGADOR2(2);

        private int mValor;

        private Estado(int valor) {
        	mValor = valor;
        }

        public int getValue() {
            return mValor;
        }

        public static Estado fromInt(int i) {
            for (Estado s : values()) {
                if (s.getValue() == i) {
                    return s;
                }
            }
            return VACIO;
        }
    }

    private static final int MARGEN = 4;
    private static final int MENSAJE_ACCION = 1;

    private final Handler mensajesEncargado = new Handler(new MyHandler());

    private final Rect mSrcRect = new Rect();
    private final Rect mDstRect = new Rect();

    private int mSxy;
    private int mOffetX;
    private int mOffetY;
    private Paint mGanarPintar;
    private Paint mLineaPintar;
    private Paint mBmpPintar;
    private Bitmap mBmpJugador1;
    private Bitmap mBmpJugador2;
    private Drawable mDrawableFondo;

    private TelefonoEstadoEscucha mAccionTelefono;

    /** Contiene uno de los siguientes estados
     * 
     *   {@link Estado#EMPTY}, {@link Estado#PLAYER1} or {@link Estado#PLAYER2}. */
    private final Estado[] mDatos = new Estado[9];

    private int mSelectedCell = -1;
    private Estado mSelectedValue = Estado.VACIO;
    private Estado mJugadorActual = Estado.DESCONOCIDO;
    private Estado mWinner = Estado.VACIO;

    private int mGanarColumna = -1;
    private int mGanarFila = -1;
    private int mGanarDiagonal = -1;

    private boolean mDesactivarAvisoLinea;
    private final Rect mBlinkRect = new Rect();



    public interface TelefonoEstadoEscucha {
        abstract void onAccionTelefono();
    }

    public VistaJuego(Context contexto, AttributeSet atributos) {
        super(contexto, atributos);
        requestFocus();

        mDrawableFondo = getResources().getDrawable(R.drawable.fondo);
        setBackgroundDrawable(mDrawableFondo);

        mBmpJugador1 = getResBitmap(R.drawable.cruz);
        mBmpJugador2 = getResBitmap(R.drawable.circulo);

        if (mBmpJugador1 != null) {
            mSrcRect.set(0, 0, mBmpJugador1.getWidth() -1, mBmpJugador1.getHeight() - 1);
        }

        mBmpPintar = new Paint(Paint.ANTI_ALIAS_FLAG);

        mLineaPintar = new Paint();
        mLineaPintar.setColor(0xFFFFFFFF);
        mLineaPintar.setStrokeWidth(5);
        mLineaPintar.setStyle(Style.STROKE);

        mGanarPintar = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGanarPintar.setColor(0xFFFF0000);
        mGanarPintar.setStrokeWidth(10);
        mGanarPintar.setStyle(Style.STROKE);

        for (int i = 0; i < mDatos.length; i++) {
        	mDatos[i] = Estado.VACIO;
        }

        if (isInEditMode()) {
            Random rnd = new Random();
            for (int i = 0; i < mDatos.length; i++) {
            	mDatos[i] = Estado.fromInt(rnd.nextInt(3));
            }
        }
    }

    public Estado[] getData() {
        return mDatos;
    }

    public void setTelefono(int telefonoIndex, Estado valor) {
    	mDatos[telefonoIndex] = valor;
        invalidate();
    }

    public void setTelefonoAccion(TelefonoEstadoEscucha telefonoAccion) {
    	mAccionTelefono = telefonoAccion;
    }

    public int getSelection() {
        if (mSelectedValue == mJugadorActual) {
            return mSelectedCell;
        }

        return -1;
    }

    public Estado getJugadorActual() {
        return mJugadorActual;
    }

    public void setJugadorActual(Estado jugador) {
    	mJugadorActual = jugador;
        mSelectedCell = -1;
    }

    public Estado getWinner() {
        return mWinner;
    }

    public void setWinner(Estado winner) {
        mWinner = winner;
    }

    
    public void setFinished(int col, int row, int diagonal) {
    	mGanarColumna = col;
    	mGanarFila = row;
    	mGanarDiagonal = diagonal;
    }

    //-----------------------------------------


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int sxy = mSxy;
        int s3  = sxy * 3;
        int x7 = mOffetX;
        int y7 = mOffetY;

        for (int i = 0, k = sxy; i < 2; i++, k += sxy) {
            canvas.drawLine(x7    , y7 + k, x7 + s3 - 1, y7 + k     , mLineaPintar);
            canvas.drawLine(x7 + k, y7    , x7 + k     , y7 + s3 - 1, mLineaPintar);
        }

        for (int j = 0, k = 0, y = y7; j < 3; j++, y += sxy) {
            for (int i = 0, x = x7; i < 3; i++, k++, x += sxy) {
                mDstRect.offsetTo(MARGEN+x, MARGEN+y);

                Estado v;
                if (mSelectedCell == k) {
                    if (mDesactivarAvisoLinea) {
                        continue;
                    }
                    v = mSelectedValue;
                } else {
                    v = mDatos[k];
                }

                switch(v) {
                case JUGADOR1:
                    if (mBmpJugador1 != null) {
                        canvas.drawBitmap(mBmpJugador1, mSrcRect, mDstRect, mBmpPintar);
                    }
                    break;
                case JUGADOR2:
                    if (mBmpJugador2 != null) {
                        canvas.drawBitmap(mBmpJugador2, mSrcRect, mDstRect, mBmpPintar);
                    }
                    break;               	
				default:
					break;
                }
            }
        }

        if (mGanarFila >= 0) {
            int y = y7 + mGanarFila * sxy + sxy / 2;
            canvas.drawLine(x7 + MARGEN, y, x7 + s3 - 1 - MARGEN, y, mGanarPintar);

        } else if (mGanarColumna >= 0) {
            int x = x7 + mGanarColumna * sxy + sxy / 2;
            canvas.drawLine(x, y7 + MARGEN, x, y7 + s3 - 1 - MARGEN, mGanarPintar);

        } else if (mGanarDiagonal == 0) {
            // diagonal 0 es de (0,0) hasta (2,2)

            canvas.drawLine(x7 + MARGEN, y7 + MARGEN,
                    x7 + s3 - 1 - MARGEN, y7 + s3 - 1 - MARGEN, mGanarPintar);

        } else if (mGanarDiagonal == 1) {
            // diagonal 1 es desde (0,2) hasta (2,0)

            canvas.drawLine(x7 + MARGEN, y7 + s3 - 1 - MARGEN,
                    x7 + s3 - 1 - MARGEN, y7 + MARGEN, mGanarPintar);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Para poder mantener cuadrada la vista
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int viejoAncho, int viejaAltura) {
        super.onSizeChanged(ancho, alto, viejoAncho, viejaAltura);

        int sx = (ancho - 2 * MARGEN) / 3;
        int sy = (alto - 2 * MARGEN) / 3;

        int size = sx < sy ? sx : sy;

        mSxy = size;
        mOffetX = (ancho - 3 * size) / 2;
        mOffetY = (alto - 3 * size) / 2;

        mDstRect.set(MARGEN, MARGEN, size - MARGEN, size - MARGEN);
    }

    @Override
    public boolean onTouchEvent(MotionEvent evento) {
        int accion = evento.getAction();

        if (accion == MotionEvent.ACTION_DOWN) {
            return true;

        } else if (accion == MotionEvent.ACTION_UP) {
            int x = (int) evento.getX();
            int y = (int) evento.getY();

            int sxy = mSxy;
            x = (x - MARGEN) / sxy;
            y = (y - MARGEN) / sxy;

            if (isEnabled() && x >= 0 && x < 3 && y >= 0 & y < 3) {
                int telefono = x + 3 * y;

                Estado estado = telefono == mSelectedCell ? mSelectedValue : mDatos[telefono];
                estado = estado == Estado.VACIO ? mJugadorActual : Estado.VACIO;

                stopBlink();

                mSelectedCell = telefono;
                mSelectedValue = estado;
                mDesactivarAvisoLinea = false;
                mBlinkRect.set(MARGEN + x * sxy, MARGEN + y * sxy,
                			   MARGEN + (x + 1) * sxy, MARGEN + (y + 1) * sxy);

                if (estado != Estado.VACIO) {
                    // Start the blinker
                	mensajesEncargado.sendEmptyMessageDelayed(MENSAJE_ACCION, FPS_MS);
                }

                if (mAccionTelefono != null) {
                	mAccionTelefono.onAccionTelefono();
                }
            }

            return true;
        }

        return false;
    }

    public void stopBlink() {
        boolean hadSelection = mSelectedCell != -1 && mSelectedValue != Estado.VACIO;
        mSelectedCell = -1;
        mSelectedValue = Estado.VACIO;
        if (!mBlinkRect.isEmpty()) {
            invalidate(mBlinkRect);
        }
        mDesactivarAvisoLinea = false;
        mBlinkRect.setEmpty();
        mensajesEncargado.removeMessages(MENSAJE_ACCION);
        if (hadSelection && mAccionTelefono != null) {
        	mAccionTelefono.onAccionTelefono();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle b = new Bundle();

        Parcelable s = super.onSaveInstanceState();
        b.putParcelable("gv_super_state", s);

        b.putBoolean("gv_en", isEnabled());

        int[] data = new int[mDatos.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = mDatos[i].getValue();
        }
        b.putIntArray("gv_data", data);

        b.putInt("gv_sel_cell", mSelectedCell);
        b.putInt("gv_sel_val",  mSelectedValue.getValue());
        b.putInt("gv_curr_play", mJugadorActual.getValue());
        b.putInt("gv_winner", mWinner.getValue());

        b.putInt("gv_win_col", mGanarColumna);
        b.putInt("gv_win_row", mGanarFila);
        b.putInt("gv_win_diag", mGanarDiagonal);

        b.putBoolean("gv_blink_off", mDesactivarAvisoLinea);
        b.putParcelable("gv_blink_rect", mBlinkRect);

        return b;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (!(state instanceof Bundle)) {
            // Not supposed to happen.
            super.onRestoreInstanceState(state);
            return;
        }

        Bundle b = (Bundle) state;
        Parcelable superState = b.getParcelable("gv_super_state");

        setEnabled(b.getBoolean("gv_en", true));

        int[] data = b.getIntArray("gv_data");
        if (data != null && data.length == mDatos.length) {
            for (int i = 0; i < data.length; i++) {
            	mDatos[i] = Estado.fromInt(data[i]);
            }
        }

        mSelectedCell = b.getInt("gv_sel_cell", -1);
        mSelectedValue = Estado.fromInt(b.getInt("gv_sel_val", Estado.VACIO.getValue()));
        mJugadorActual = Estado.fromInt(b.getInt("gv_curr_play", Estado.VACIO.getValue()));
        mWinner = Estado.fromInt(b.getInt("gv_winner", Estado.VACIO.getValue()));

        mGanarColumna = b.getInt("gv_win_col", -1);
        mGanarFila = b.getInt("gv_win_row", -1);
        mGanarDiagonal = b.getInt("gv_win_diag", -1);

        mDesactivarAvisoLinea = b.getBoolean("gv_blink_off", false);
        Rect r = b.getParcelable("gv_blink_rect");
        if (r != null) {
            mBlinkRect.set(r);
        }

        // let the blink handler decide if it should blink or not
        mensajesEncargado.sendEmptyMessage(MENSAJE_ACCION);

        super.onRestoreInstanceState(superState);
    }

    //-----

    private class MyHandler implements Callback {
        public boolean handleMessage(Message msg) {
            if (msg.what == MENSAJE_ACCION) {
                if (mSelectedCell >= 0 && mSelectedValue != Estado.VACIO && mBlinkRect.top != 0) {
                	mDesactivarAvisoLinea = !mDesactivarAvisoLinea;
                    invalidate(mBlinkRect);

                    if (!mensajesEncargado.hasMessages(MENSAJE_ACCION)) {
                    	mensajesEncargado.sendEmptyMessageDelayed(MENSAJE_ACCION, FPS_MS);
                    }
                }
                return true;
            }
            return false;
        }
    }

    private Bitmap getResBitmap(int bmpResId) {
        Options opts = new Options();
        opts.inDither = false;

        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, bmpResId, opts);

        if (bmp == null && isInEditMode()) {
            // BitmapFactory.decodeResource doesn't work from the rendering
            // library in Eclipse's Graphical Layout Editor. Use this workaround instead.

            Drawable d = res.getDrawable(bmpResId);
            int w = d.getIntrinsicWidth();
            int h = d.getIntrinsicHeight();
            bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
            Canvas c = new Canvas(bmp);
            d.setBounds(0, 0, w - 1, h - 1);
            d.draw(c);
        }

        return bmp;
    }
}
