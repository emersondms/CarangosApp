package br.com.caelum.fj59.carangos.tasks;

import android.os.AsyncTask;

import java.io.Serializable;
import java.util.List;

import br.com.caelum.fj59.carangos.app.CarangosApplication;
import br.com.caelum.fj59.carangos.converter.PublicacaoConverter;
import br.com.caelum.fj59.carangos.evento.EventoPublicacoesRecebidas;
import br.com.caelum.fj59.carangos.modelo.Publicacao;
import br.com.caelum.fj59.carangos.webservice.Pagina;
import br.com.caelum.fj59.carangos.webservice.WebClient;

public class BuscaMaisPublicacoesTask extends AsyncTask<Pagina, Void, List<Publicacao>> {

    private Exception erro;
    private CarangosApplication application;

    public BuscaMaisPublicacoesTask(CarangosApplication application) {
        this.application = application;
        application.registra(this);
    }

    @Override
    protected List<Publicacao> doInBackground(Pagina... paginas) {
        try {
            Pagina paginaParaBuscar = paginas.length > 1 ?
                paginas[0] :
                new Pagina();

            String jsonDeResposta = new WebClient(
                "post/list?" + paginaParaBuscar, application
            ).get();

            List<Publicacao> publicacoesRecebidas = new PublicacaoConverter().converte(
                jsonDeResposta
            );

            return publicacoesRecebidas;
        } catch (Exception e) {
            this.erro = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Publicacao> retorno) {
        if (retorno != null) {
            EventoPublicacoesRecebidas.notifica(
                this.application, (Serializable) retorno, true
            );
        } else {
            EventoPublicacoesRecebidas.notifica(
                this.application, erro, false
            );
        }

        this.application.desregistra(this);
    }

}
