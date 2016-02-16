package chikachi.interactive.common.action;

import chikachi.interactive.common.Game;
import chikachi.interactive.common.input.InputBase;
import chikachi.interactive.common.input.InputJoystick;
import chikachi.interactive.common.input.InputTactile;
import chikachi.lib.common.utils.ReflectionUtils;
import net.minecraft.entity.player.EntityPlayer;
import pro.beam.interactive.net.packet.Protocol;
import pro.beam.interactive.net.packet.Protocol.ProgressUpdate;
import pro.beam.interactive.robot.Robot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// TODO: Add support for joystick
public class ActionManager {

    private final Game game;
    private Map<InputBase, ActionBase> actions = new HashMap<InputBase, ActionBase>();

    public ActionManager(Game game) {
        this.game = game;
    }

    private ProgressUpdate.TactileUpdate getTactileProcess(Map.Entry<InputBase, ActionBase> entry, boolean fired) {
        ProgressUpdate.TactileUpdate.Builder builder = ProgressUpdate.TactileUpdate.newBuilder()
                .setFired(fired);

        builder.setId(entry.getKey().getId());

        if (entry.getValue().hasCooldown()) {
            builder.setCooldown(entry.getValue().getCooldown());
        }

        return builder.build();
    }

    private ProgressUpdate.JoystickUpdate getJoystickProcess(Map.Entry<InputBase, ActionBase> entry) {
        ProgressUpdate.JoystickUpdate.Builder builder = ProgressUpdate.JoystickUpdate.newBuilder();

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

        for (Map.Entry<InputBase, ActionBase> inputActionEntry : this.actions.entrySet()) {
            if (!inputActionEntry.getKey().isMet(event)) {
                continue;
            }

            if (inputActionEntry.getKey() instanceof InputTactile) {
                progress.addTactile(getTactileProcess(inputActionEntry, inputActionEntry.getValue().execute()));
            } else if (inputActionEntry.getKey() instanceof InputJoystick) {
                inputActionEntry.getValue().execute();
                progress.addJoystick(getJoystickProcess(inputActionEntry));
            }
        }

        if (progress.getTactileCount() > 0) {
            Robot robot = game.getRobot();

            if (robot != null && robot.isOpen()) {
                robot.write(progress.build());
            }
        }
    }

    public EntityPlayer getEntityPlayer() {
        return game.getEntityPlayer();
    }

    public void setActions(Map<InputBase, ActionBase> actions) {
        this.actions = new HashMap<InputBase, ActionBase>();

        for (Map.Entry<InputBase, ActionBase> actionEntry : actions.entrySet()) {
            if (actionEntry.getKey() == null || actionEntry.getValue() == null) return;

            this.addAction(actionEntry.getKey(), actionEntry.getValue());
        }
    }

    public void addAction(InputBase input, ActionBase action) {
        if (input == null || action == null) return;

        action.setManager(this);
        this.actions.put(input, action);
    }

    public Map<InputBase, ActionBase> getActions() {
        return actions;
    }
}