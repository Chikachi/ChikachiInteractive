package chikachi.interactive.common;

import chikachi.interactive.common.action.ActionBase;
import chikachi.interactive.common.action.ActionManager;
import chikachi.interactive.common.input.InputBase;
import chikachi.interactive.common.tetris.TetrisForgeConnector;
import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import pro.beam.api.BeamAPI;
import pro.beam.api.exceptions.BeamException;
import pro.beam.api.resource.BeamUser;
import pro.beam.api.services.impl.UsersService;
import pro.beam.interactive.robot.Robot;
import pro.beam.interactive.robot.RobotBuilder;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/*
TODO: Add support multiple streamers on same server
TODO: Add support for different interactive games
TODO: Change the main config file to be JSON instead
*/
public class Game {
    private String minecraftName;
    private String username;
    private String password;
    private String twoFactor;
    private int channelId;

    private BeamUser beamUserInstance;
    private Robot robotInstance;

    private ActionManager actionManager;

    public Game(String minecraftName, String username, String password, String twoFactor, int channelId, Map<InputBase, ActionBase> actions) {
        this.minecraftName = minecraftName;
        this.username = username;
        this.password = password;
        this.twoFactor = twoFactor;
        this.channelId = channelId;

        this.actionManager = new ActionManager(this);

        setActions(actions);
    }

    public void setActions(Map<InputBase, ActionBase> actions) {
        this.actionManager.setActions(actions);
    }

    public EntityPlayer getEntityPlayer() {
        if (this.minecraftName != null && !this.minecraftName.isEmpty()) {
            WorldServer[] worldServers = MinecraftServer.getServer().worldServers;
            for (WorldServer worldServer : worldServers) {
                EntityPlayer player = worldServer.getPlayerEntityByName(this.minecraftName);
                if (player != null) {
                    return player;
                }
            }
        }

        return null;
    }

    public BeamUser getBeamUser() {
        if (beamUserInstance == null) {
            BeamAPI beam = TetrisForgeConnector.getInstance().getBeam();

            CheckedFuture<BeamUser, BeamException> task;
            if (twoFactor.length() == 6) {
                task = beam.use(UsersService.class).login(username, password, twoFactor);
            } else {
                task = beam.use(UsersService.class).login(username, password);
            }

            try {
                beamUserInstance = task.checkedGet();
            } catch (BeamException e) {
                beamUserInstance = null;
            }
        }

        return beamUserInstance;
    }

    public Robot getRobotInstance() {
        return this.robotInstance;
    }

    public Robot getRobot() {
        return getRobot(false);
    }

    public Robot getRobot(boolean forceNewInstance) {
        if (forceNewInstance || this.robotInstance == null) {
            BeamAPI beam = TetrisForgeConnector.getInstance().getBeam();
            RobotBuilder builder = new RobotBuilder();

            builder.username(username)
                    .password(password)
                    .channel(channelId);

            if (twoFactor.length() == 6) {
                builder.twoFactor(twoFactor);
            }

            try {
                this.robotInstance = builder.build(beam).get();
            } catch (InterruptedException e) {
                this.robotInstance = null;
            } catch (ExecutionException e) {
                this.robotInstance = null;
            }
        }

        return this.robotInstance;
    }

    public ActionManager getManager() {
        return this.actionManager;
    }
}
