package chikachi.interactive.common.input;

import pro.beam.interactive.net.packet.Protocol;

public class InputTactile extends InputBase {
    private double lastHoldingCount = 0;

    public InputTactile(int id) {
        super(id);
    }

    @Override
    public boolean isMet(int id) {
        return id == this.id;
    }

    @Override
    public boolean isMet(Protocol.Report event) {
        for (Protocol.Report.TactileInfo info : event.getTactileList()) {
            if (info.getId() == this.id) {
                double holdingCount = info.getHolding();
                boolean trigger = holdingCount < this.lastHoldingCount;
                this.lastHoldingCount = holdingCount;
                return trigger;
            }
        }

        return false;
    }
}
