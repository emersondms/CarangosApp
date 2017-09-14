package br.com.caelum.fj59.carangos.tasks;

import java.util.List;

import br.com.caelum.fj59.carangos.app.CarangosApplication;
import br.com.caelum.fj59.carangos.modelo.Publicacao;

public interface BuscaMaisPublicacoesDelegate {

    void lidaComRetorno(List<Publicacao> retorno);
    void lidaComErro(Exception e);

    CarangosApplication getCarangosApplication();

}
