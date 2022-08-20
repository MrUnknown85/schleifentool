/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ELschleifentool.network;

import ELschleifentool.model.BotschutzClickListener;
import ELschleifentool.model.BotschutzInterface;
import ELschleifentool.model.BotschutzParams;
import ELschleifentool.model.BotschutzResponse;
import ELschleifentool.util.Callback;
import ELschleifentool.util.Log;
import ELschleifentool.util.Pair;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 *
 * @author Nico
 * @param <T>
 */
public class ExecutorThread<T extends BotschutzResponse> extends Thread {

    private final BotschutzInterface botschutzInterface;
    private final BlockingQueue<Pair<Call<T>, Callback<T>>> queue = new LinkedBlockingQueue<>();
    private final BotschutzClickListener botschutzClickListener;
    boolean done = true;

    public ExecutorThread(BotschutzInterface botschutzInterface, BotschutzClickListener botschutzClickListener) {
        this.botschutzInterface = botschutzInterface;
        this.botschutzClickListener = botschutzClickListener;
    }

    public synchronized void queueRequest(Call<T> obs, Callback<T> action) {
//        Log.d("ExecutorThread queueRequest");
        try {
            queue.put(new Pair<>(obs, action));
        } catch (InterruptedException ex) {
            Logger.getLogger(ExecutorThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void setDone() {
//        Log.d("ExecutorThread setDone");
        done = true;

        this.notifyAll();
    }

    public synchronized void waitUntilDone() {
//        Log.d("ExecutorThread waitUntilDone");
        while (!done) {

            try {
                this.wait();

            } catch (InterruptedException ignore) {
                // log.debug("interrupted: " + ignore.getMessage());
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!queue.isEmpty()) {
                Pair<Call<T>, Callback<T>> pair = queue.poll();
                Call<T> obs = pair.getKey();
                Callback<T> action = pair.getValue();
                execute(obs, action);
            }
        }
    }

    private void execute(Call<T> obs, Callback<T> action) {
//        Log.d("ExecutorThread execute");
        try {
            retrofit2.Response<T> rspns = obs.execute();
            if (rspns.isSuccessful()) {
                T r = rspns.body();
                assert r != null;
                try {
//                    Log.d("ExecutorThread age hasBotschutz=" + r.hasBotschutz);
                    if (r.hasBotschutz) {
                        waitUntilDone();
                        botschutzInterface.showBotschutzImage(r.sid, r.captchaId, (BotschutzParams params) -> {
                            botschutzClickListener.onBotschutzClicked(params);
                            execute(obs.clone(), action);
                            setDone();
                        });

                    } else if (action != null) {
                        action.accept(r);
                    }
//                        else if (doLogout && ausgeloggt) {
//                            ELTools.getLogoutInterface().onLoggedout();
//                        } else if (doLogout && ipAbgelaufen) {
//                            ELTools.getLogoutInterface().onIpChanged();
//                        } else {
//                            action.accept(t);
//                        }
                } catch (Exception ex) {
                    Log.err(ex);
                }

            } else {
                try {
                    ResponseBody errorBody = rspns.errorBody();
                    if (errorBody != null) {
                        Log.err(new Throwable(errorBody.string()));
                    }
                } catch (IOException ex) {
                    Log.err(ex);
                }
                // action.onNext(null);
            }
        } catch (IOException ex) {
            Log.err(ex);
            // action.onNext(null);
        }
    }

}
