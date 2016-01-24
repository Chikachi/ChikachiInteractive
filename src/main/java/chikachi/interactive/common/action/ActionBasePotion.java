package chikachi.interactive.common.action;

import chikachi.lib.common.utils.MapUtils;

public abstract class ActionBasePotion extends ActionBase {
    protected boolean stackable = false;
    protected int duration = 10;
    protected int amplifier = 0;

    protected void setPotionData(MapUtils<String> utils) {
        this.stackable = utils.getBoolean("stackable", false);
        this.duration = utils.getInteger("duration", 10);
        this.amplifier = utils.getInteger("amplifier", 0);
    }
}
