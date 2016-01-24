package chikachi.interactive.common.input;

import pro.beam.interactive.net.packet.Protocol;

public abstract class InputBase {
    public boolean isMet(int code) {
        return false;
    }
    public boolean isMet(Protocol.Report event) {
        return false;
    }
}
