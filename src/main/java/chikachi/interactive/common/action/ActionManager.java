package chikachi.interactive.common.action;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.Game;
import chikachi.interactive.common.input.InputBase;
import chikachi.interactive.common.input.InputTactile;
import net.minecraft.entity.player.EntityPlayer;
import pro.beam.interactive.net.packet.Protocol;
import pro.beam.interactive.net.packet.Protocol.ProgressUpdate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ActionManager {
    private final Game game;
    private Map<InputBase, ActionBase> actions;

    public ActionManager(Game game) {
        this.game = game;
        this.actions = new HashMap<InputBase, ActionBase>();
    }

    private ProgressUpdate.Progress getProcess(Map.Entry<InputBase, ActionBase> entry, boolean fired) {
        ProgressUpdate.Progress.Builder builder = ProgressUpdate.Progress.newBuilder()
                .setFired(fired);

        if (entry.getKey() instanceof InputTactile) {
            builder.setCode(((InputTactile) entry.getKey()).getCode());
        }

        if (entry.getValue().hasCooldown()) {
            builder.setCooldown(entry.getValue().getCooldown());
        }

        return builder.build();
    }

    public void dispatch(int code) {
        for (Map.Entry<InputBase, ActionBase> actionEntry : this.actions.entrySet()) {
            if (!actionEntry.getKey().isMet(code)) {
                continue;
            }

            actionEntry.getValue().execute();
        }
    }

    public void dispatch(Protocol.Report event) throws IOException {
        ProgressUpdate.Builder progress = Protocol.ProgressUpdate.newBuilder();

        for (Map.Entry<InputBase, ActionBase> actionEntry : this.actions.entrySet()) {
            if (!actionEntry.getKey().isMet(event)) {
                continue;
            }

            progress.addProgress(getProcess(actionEntry, actionEntry.getValue().execute()));
        }

        if (progress.getProgressCount() > 0) {
            ChikachiInteractive.instance.robot.write(progress.build());
        }
    }

    public EntityPlayer getEntityPlayer() {
        return game.getEntityPlayer();
    }

    public void setActions(Map<InputBase, ActionBase> actions) {
        this.actions = new HashMap<InputBase, ActionBase>();
        for (Map.Entry<InputBase, ActionBase> actionEntry : actions.entrySet()) {
            this.addAction(actionEntry.getKey(), actionEntry.getValue());
        }
    }

    public void addAction(InputBase input, ActionBase action) {
        action.setManager(this);
        this.actions.put(input, action);
    }
}