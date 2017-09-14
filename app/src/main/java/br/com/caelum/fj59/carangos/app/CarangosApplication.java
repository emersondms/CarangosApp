package br.com.caelum.fj59.carangos.app;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.fj59.carangos.infra.MyLog;
import br.com.caelum.fj59.carangos.modelo.Publicacao;
import br.com.caelum.fj59.carangos.tasks.RegistraAparelhoTask;

public class CarangosApplication extends Application {

    private List<AsyncTask<?, ?, ?>> tasks = new ArrayList<>();
    private List<Publicacao> publicacoes = new ArrayList<>();
    private static final String REGISTRADO = "registradoNoGCM";
    private static final String ID_DO_REGISTRO = "idDoRegistro";
    private SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = getSharedPreferences("configs", Activity.MODE_PRIVATE);

        registraNoGCM();
    }

    private void registraNoGCM() {
        if (!usuarioRegistrado()) {
            new RegistraAparelhoTask(this).execute();
        } else {
            MyLog.i(
                "APARELHO JA CADASTRADO! SEU ID É: "
                    + preferences.getString(ID_DO_REGISTRO, null)
            );
        }
    }

    private boolean usuarioRegistrado() {
        return preferences.getBoolean(REGISTRADO, false);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        for (AsyncTask task : this.tasks) {
            task.cancel(true);
        }
    }

    public void registra(AsyncTask<?, ?, ?> task) {
        tasks.add(task);
    }

    public void desregistra(AsyncTask<?, ?, ?> task) {
        tasks.remove(task);
    }

    public List<Publicacao> getPublicacoes() {
        return publicacoes;
    }

    public void lidaComRepostaDoRegistroNoServidor(String registro) {
        if (registro != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(REGISTRADO, true);
            editor.putString(ID_DO_REGISTRO, registro);
            editor.commit();
        }
    }

}
