package chikachi.interactive.common;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.action.ActionBase;
import chikachi.interactive.common.action.ActionManager;
import chikachi.interactive.common.input.InputBase;
import chikachi.interactive.common.tetris.TetrisForgeConnector;
import com.google.common.util.concurrent.CheckedFuture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import pro.beam.api.BeamAPI;
import pro.beam.api.exceptions.BeamException;
import pro.beam.api.resource.BeamUser;
import pro.beam.api.services.impl.UsersService;
import pro.beam.interactive.robot.Robot;
import pro.beam.interactive.robot.RobotBuilder;

import java.io.IOException;
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
        if (this.beamUserInstance == null) {
            BeamAPI beam = TetrisForgeConnector.getInstance().getBeam();

            CheckedFuture<BeamUser, BeamException> task;
            if (this.twoFactor != null && this.twoFactor.length() == 6) {
                task = beam.use(UsersService.class).login(this.username, this.password, this.twoFactor);
            } else {
                task = beam.use(UsersService.class).login(this.username, this.password);
            }

            try {
                this.beamUserInstance = task.checkedGet();
            } catch (BeamException e) {
                this.beamUserInstance = null;
            }
        }

        return this.beamUserInstance;
    }

    public Robot getRobotInstance() {
        return this.robotInstance;
    }

    public Robot getRobot() {
        return getRobot(false);
    }

    public Robot getRobot(boolean forceNewInstance) {
        return getRobot(forceNewInstance, null);
    }

    public Robot getRobot(boolean forceNewInstance, String twoFactor) {
        if (forceNewInstance || this.robotInstance == null) {
            BeamAPI beam = TetrisForgeConnector.getInstance().getBeam();
            RobotBuilder builder = new RobotBuilder();

            builder.username(this.username)
                    .password(this.password)
                    .channel(this.channelId);

            String _twoFactor = this.twoFactor;

            if (twoFactor != null) {
                _twoFactor = twoFactor;
            }

            if (_twoFactor != null && _twoFactor.length() == 6) {
                builder.twoFactor(twoFactor);
            }

            try {
                this.robotInstance = builder.build(beam).get();
            } catch (InterruptedException e) {
                ChikachiInteractive.Log("Could not open robot connection", true);
                e.printStackTrace();
                this.robotInstance = null;
            } catch (ExecutionException e) {
                ChikachiInteractive.Log("Could not open robot connection", true);
                e.printStackTrace();
                this.robotInstance = null;
            }
        }

        return this.robotInstance;
    }

    public ActionManager getManager() {
        return this.actionManager;
    }

    public void clearRobotInstance() {
        if (this.robotInstance == null) {
            return;
        }

        if (this.robotInstance.isClosed()) {
            this.robotInstance = null;
        } else {
            try {
                this.robotInstance.disconnect();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.robotInstance = null;
            }
        }
    }
}
