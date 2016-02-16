package chikachi.interactive.common.input;

import pro.beam.interactive.net.packet.Protocol;

// TODO: Finish this
public class InputJoystick extends InputBase {
    public InputJoystick(int id) {
        super(id);
    }

    @Override
    public boolean isMet(Protocol.Report event) {
        for (Protocol.Report.JoystickInfo info : event.getJoystickList()) {
            if (info.getId() == this.id) {
                return true;
            }
        }

        return false;
    }
}
