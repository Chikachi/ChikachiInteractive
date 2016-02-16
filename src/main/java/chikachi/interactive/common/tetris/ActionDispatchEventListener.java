package chikachi.interactive.common.tetris;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.action.ActionManager;
import pro.beam.interactive.event.EventListener;
import pro.beam.interactive.net.packet.Protocol;

import java.io.IOException;

public class ActionDispatchEventListener implements EventListener<Protocol.Report> {
    protected final ActionManager manager;

    public ActionDispatchEventListener(ActionManager manager) {
        this.manager = manager;
    }

    public void handle(Protocol.Report event) {
        try {
            this.manager.dispatch(event);
        } catch (IOException e) {
            ChikachiInteractive.Log("Failed to handle report", true);
            e.printStackTrace();
        }
    }
}
