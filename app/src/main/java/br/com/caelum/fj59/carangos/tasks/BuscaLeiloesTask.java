package br.com.caelum.fj59.carangos.tasks;

import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import br.com.caelum.fj59.carangos.app.CarangosApplication;
import br.com.caelum.fj59.carangos.infra.MyLog;
import br.com.caelum.fj59.carangos.webservice.WebClient;

public class BuscaLeiloesTask extends TimerTask {

    private CustomHandler handler;
    private Calendar horarioUltimaBusca;

    public BuscaLeiloesTask(CustomHandler handler, Calendar horarioUltimaBusca) {
        this.handler = handler;
        this.horarioUltimaBusca = horarioUltimaBusca;
    }

    @Override
    public void run() {
        MyLog.i("EFETUANDO NOVA BUSCA!");

        WebClient webClient = new WebClient(
            "leilao/leilaoid54635/" + new SimpleDateFormat("ddMMyy-HHmmss")
                .format(horarioUltimaBusca.getTime()),
            new CarangosApplication()
        );

        String json = webClient.get();

        MyLog.i("LANCES RECEBIDOS: " + json);

        Message message = handler.obtainMessage();
        message.obj = json;
        handler.sendMessage(message);

        horarioUltimaBusca = Calendar.getInstance();
    }

    public void executa() {
        Timer timer = new Timer();
        timer.schedule(this, 0, 30 * 1000);
    }

}
