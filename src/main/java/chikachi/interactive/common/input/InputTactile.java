package chikachi.interactive.common.input;

import pro.beam.interactive.net.packet.Protocol;

public class InputTactile extends InputBase {
    protected final int code;

    public InputTactile(int code) {
        this.code = code;
    }

    @Override
    public boolean isMet(int code) {
        return code == this.code;
    }

    @Override
    public boolean isMet(Protocol.Report event) {
        for (Protocol.Report.TactileInfo info : event.getTactileList()) {
            if (info.getCode() == this.code) {
                return true;
            }
        }

        return false;
    }

    public int getCode() {
        return this.code;
    }
}
