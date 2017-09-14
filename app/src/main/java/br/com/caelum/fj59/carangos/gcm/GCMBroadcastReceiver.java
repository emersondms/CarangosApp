package br.com.caelum.fj59.carangos.gcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

import br.com.caelum.fj59.carangos.R;
import br.com.caelum.fj59.carangos.activity.LeilaoActivity;
import br.com.caelum.fj59.carangos.infra.MyLog;

public class GCMBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MyLog.i("CHEGOU A MENSAGEM DO GCM!");

        String mensagem = (String) intent.getExtras().getSerializable("message");

        MyLog.i("MENSAGEM COM CONTEÚDO: " + mensagem);

        if (appEstaRodando(context)) {
            Toast.makeText(context, "UM NOVO LEILÃO COMEÇOU!", Toast.LENGTH_LONG).show();
        } else {
            Intent irParaLeilao = new Intent(context, LeilaoActivity.class);

            PendingIntent acaoPendente = PendingIntent.getActivity(
                context, 0, irParaLeilao, PendingIntent.FLAG_CANCEL_CURRENT
            );

            irParaLeilao.putExtra("idDaNotificacao", Constantes.ID_NOTIFICACAO);

            Notification notificacao = new Notification.Builder(context)
                .setContentTitle("UM NOVO LEILÃO COMEÇOU!")
                .setContentText("LEILÃO: " + mensagem)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(acaoPendente)
                .setAutoCancel(true)
                .build();

            NotificationManager manager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE
            );

            manager.notify(Constantes.ID_NOTIFICACAO, notificacao);
        }
    }

    private boolean appEstaRodando(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(
            Context.ACTIVITY_SERVICE
        );

        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;

            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return false;
            }
        }

        return true;
    }

}
