package com.example.user.multitarea;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn,btn2,btn3,btn4,btn5;
    ProgressBar pro;
    MiTarea tarea1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btnSinHilos);
        btn2 = (Button) findViewById(R.id.btnConHilos);
        btn3 = (Button) findViewById(R.id.btnotratarea);
        btn4 = (Button) findViewById(R.id.btnAsyncTask);
        btn5 = (Button) findViewById(R.id.btnCancelarAsyncTask);

        pro = (ProgressBar) findViewById(R.id.pbarProgreso);

        btn.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
    }

    public void demora(){
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnSinHilos:
                pro.setMax(100);
                pro.setProgress(0);

                int numbers[] = Numeros.listaNumeros;
                int aux;
                double porc;

                for (int i = 0; i < numbers.length - 1; i++) {
                    for (int j = 0; j < numbers.length -1; j++) {
                        if (numbers[j] > numbers[j+1]) {
                            aux          = numbers[j];
                            numbers[j]   = numbers[j+1];
                            numbers[j+1] = aux;
                        }

                    }

                    porc = Math.ceil(((double) i / (double)numbers.length)*100);
                    pro.setProgress((int)porc);
                    demora();
                }

                Toast.makeText(this,"Numeros ordenados",Toast.LENGTH_LONG).show();
                break;

            case R.id.btnConHilos:

                pro.setMax(100);
                pro.setProgress(0);

                new Thread(new Runnable() {
                    public void run() {

                        /*Tarea Larga*/
                        int numbers[] = Numeros.listaNumeros;
                        int aux;
                        double porc;

                        for (int i = 0; i < numbers.length - 1; i++) {
                            for (int j = 0; j < numbers.length -1; j++) {
                                if (numbers[j] > numbers[j+1]) {
                                    aux          = numbers[j];
                                    numbers[j]   = numbers[j+1];
                                    numbers[j+1] = aux;
                                }

                            }

                            porc = Math.ceil(((double) i / (double)numbers.length)*100);

                            pro.setProgress((int)porc);
                            demora();
                        }
                        /*Tarea Larga*/

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getBaseContext(), "¡Números Ordenados!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();

                break;

            case R.id.btnotratarea:
                Toast.makeText(this, "¡Ejecutando una segunda tarea!", Toast.LENGTH_LONG).show();
                break;

            case R.id.btnAsyncTask:
                tarea1 = new MiTarea();
                tarea1.execute();
                break;
            case R.id.btnCancelarAsyncTask:
                tarea1.cancel(true);
                break;
        }

    }

    private class MiTarea extends AsyncTask<Void,Integer,Boolean>{

        @Override
        protected void onPreExecute() {
            //se ejecuta antes de iniciar la tarea
            pro.setMax(100);
            pro.setProgress(0);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            //tarea larga o pesada
            int numbers[] = Numeros.listaNumeros;
            int aux;
            double porc;

            for (int i = 0; i < numbers.length - 1; i++) {
                for (int j = 0; j < numbers.length -1; j++) {
                    if (numbers[j] > numbers[j+1]) {
                        aux          = numbers[j];
                        numbers[j]   = numbers[j+1];
                        numbers[j+1] = aux;
                    }

                }

                porc = Math.ceil(((double) i / (double)numbers.length)*100);

                publishProgress((int)porc);
                demora();
            }
                        /*Tarea Larga*/
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        int progreso = values[0].intValue();
            pro.setProgress(progreso);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Toast.makeText(MainActivity.this, "¡Numeros ordenados!", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MainActivity.this, "¡Tarea cancelada!", Toast.LENGTH_LONG).show();
        }
    }
}
