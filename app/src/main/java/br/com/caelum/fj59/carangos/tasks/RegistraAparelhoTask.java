package br.com.caelum.fj59.carangos.tasks;

import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import br.com.caelum.fj59.carangos.app.CarangosApplication;
import br.com.caelum.fj59.carangos.gcm.Constantes;
import br.com.caelum.fj59.carangos.gcm.InformacoesDoUsuario;
import br.com.caelum.fj59.carangos.infra.MyLog;
import br.com.caelum.fj59.carangos.webservice.WebClient;

public class RegistraAparelhoTask extends AsyncTask<Void, Void, String> {

    private CarangosApplication app;

    public RegistraAparelhoTask(CarangosApplication app) {
        this.app = app;
    }

    @Override
    protected String doInBackground(Void... params) {
        String registrationID = null;

        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this.app);
            registrationID = gcm.register(Constantes.GCM_SERVER_ID);

            MyLog.i("APARELHO REGISTRADO COM O ID: " + registrationID);

            String email = InformacoesDoUsuario.getEmail(this.app);
            String url = "device/register/" + email + "/" + registrationID;
            WebClient client = new WebClient(url, app);
            client.post();
        } catch (IOException e) {
            MyLog.e("PROBLEMA NO REGISTRO DO APARELHO NO SERVER: " + e.getMessage());
        }

        return registrationID;
    }

    @Override
    protected void onPostExecute(String result) {
        app.lidaComRepostaDoRegistroNoServidor(result);
    }

}
