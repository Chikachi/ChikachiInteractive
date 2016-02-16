package chikachi.interactive.client.gui;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.action.ActionBase;
import chikachi.interactive.common.input.InputBase;
import chikachi.interactive.common.message.MessageInteractiveActionEdit;
import chikachi.interactive.common.tetris.TetrisForgeConnector;
import chikachi.lib.client.gui.ChikachiGuiScreen;
import cpw.mods.fml.client.config.GuiSelectString;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Tuple;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GuiInteractive extends ChikachiGuiScreen {
    public static Map<Integer, Tuple> inputs;

    private static final int BUTTONS_PER_ACTION = 2;

    private int editInput = -1;
    private int removeInput = -1;

    private ActionBase tempAction;

    private boolean isSelectingAction = false;

    private GuiInteractive.ActionList actionList;
    private List<ActionBase> registeredActions = TetrisForgeConnector.getRegisteredActions();

    private GuiTextField textFieldId;

    @Override
    public void initGui() {
        super.initGui();

        textFieldId = new GuiTextField(this.fontRendererObj, 25, 85, 50, 20);
        textFieldId.setMaxStringLength(5);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        if (this.textFieldId != null) {
            this.textFieldId.updateCursorCounter();
        }
    }

    @Override
    protected void keyTyped(char character, int par2) {
        super.keyTyped(character, par2);

        ChikachiInteractive.Log(Character.toString(character));

        if (Character.isDigit(character) || character == KeyEvent.VK_BACK_SPACE) {
            this.textFieldId.textboxKeyTyped(character, par2);
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) {
        super.mouseClicked(x, y, btn);

        this.textFieldId.mouseClicked(x, y, btn);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        drawRect(0, 0, this.width, this.height, 0xa0000000);

        this.drawCenteredString(this.fontRendererObj, "Chikachi Interactive", this.width / 2, 20, 16777215, 3);

        this.buttonList.clear();

        if (this.editInput > -1) {
            this.drawString(this.fontRendererObj, "Edit Action", 20, 55, 0xffffff, 1.5);

            this.buttonList.add(new GuiButton(-5, this.width - 130, 55, 50, 20, "Save"));
            this.buttonList.add(new GuiButton(-4, this.width - 75, 55, 50, 20, "Cancel"));
        } else {
            this.drawString(this.fontRendererObj, "Actions", 20, 55, 0xffffff, 1.5);

            this.buttonList.add(new GuiButton(-3, this.width - 185, 55, 50, 20, "Start"));
            this.buttonList.add(new GuiButton(-2, this.width - 130, 55, 50, 20, "Stop"));
            this.buttonList.add(new GuiButton(-1, this.width - 75, 55, 50, 20, "Add"));
        }

        if (inputs != null) {
            if (this.editInput > -1) {
                ActionBase editAction = (ActionBase) inputs.get(this.editInput).getSecond();

                if (this.isSelectingAction) {
                    this.actionList.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
                } else {
                    this.textFieldId.drawTextBox();

                    this.buttonList.add(new GuiButton(-8, 25, 110, 150, 20, TetrisForgeConnector.getActionName(editAction.getClass().getName())));

                    // TODO: Add data inputs
                }
            } else {
                int y = 85;

                Set<Map.Entry<Integer, Tuple>> actionEntries = inputs.entrySet();

                for (Map.Entry<Integer, Tuple> actionEntry : actionEntries) {
                    int inputId = actionEntry.getKey();

                    drawRect(20, y - 5, this.width - 20, y + 20, 0xa0000000);
                    this.drawString(this.fontRendererObj, Integer.toString(inputId), 25, y, 0xffffff);
                    this.drawString(this.fontRendererObj, ((ActionBase) inputs.get(inputId).getSecond()).getGuiText(), 45, y, 0xffffff);
                    this.buttonList.add(new GuiButton(BUTTONS_PER_ACTION * inputId, this.width - 130, y - 3, 50, 20, "Edit"));
                    this.buttonList.add(new GuiButton((BUTTONS_PER_ACTION * inputId) + 1, this.width - 75, y - 3, 50, 20, "Remove"));

                    y += 25;
                }
            }
        }

        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        super.actionPerformed(guiButton);

        int buttonId = guiButton.id;
        int actionId;

        switch (buttonId) {
            case -8:
                this.actionList = new GuiInteractive.ActionList();
                this.actionList.registerScrollButtons(-7, -6);
                this.isSelectingAction = true;
                break;
            case -7:
            case -6:
                // Used by action list
                this.actionList.actionPerformed(guiButton);
                break;
            case -5:
                // Save / Delete
                if (this.editInput < 0 && this.removeInput < 0) {
                    break;
                }

                actionId = this.editInput > -1 ? this.editInput : this.removeInput;

                MessageInteractiveActionEdit message = new MessageInteractiveActionEdit();
                message.setId(actionId);

                if (this.editInput > -1) {
                    message.setEditType(MessageInteractiveActionEdit.EditType.EDIT);
                } else {
                    message.setEditType(MessageInteractiveActionEdit.EditType.DELETE);
                }

                ChikachiInteractive.network.sendToServer(message);
                break;
            case -4:
                // Cancel / Don't delete
                this.editInput = -1;
                this.removeInput = -1;
                break;
            case -3:
                // Start
                ChikachiInteractive.Log("Start");
                break;
            case -2:
                // Stop
                ChikachiInteractive.Log("Stop");
                break;
            case -1:
                // New
                ChikachiInteractive.Log("New");
                break;
            default:
                // Action button
                int type = buttonId % 2;

                boolean isEdit = type == 0;
                boolean isRemove = type == 1;

                actionId = (int) Math.floor(buttonId / BUTTONS_PER_ACTION);

                ChikachiInteractive.Log(isEdit ? "Edit" : isRemove ? "Remove" : "Unknown");
                ChikachiInteractive.Log(Integer.toString(actionId));

                if (inputs.containsKey(actionId)) {
                    this.editInput = isEdit ? actionId : -1;
                    this.removeInput = isRemove ? actionId : -1;

                    if (isEdit) {
                        this.textFieldId.setFocused(true);
                        this.textFieldId.setText(Integer.toString(this.editInput));

                        this.tempAction = (ActionBase) inputs.get(actionId).getSecond();
                    }
                }
                break;
        }
    }

    private InputBase getEditInput() {
        if (this.editInput < 0 || inputs.containsKey(this.editInput)) {
            return null;
        }

        return (InputBase) inputs.get(this.editInput).getFirst();
    }

    private ActionBase getEditAction() {
        if (this.editInput < 0 || inputs.containsKey(this.editInput)) {
            return null;
        }

        return (ActionBase) inputs.get(this.editInput).getSecond();
    }

    class ActionList extends GuiSlot {
        public ActionList() {
            super(GuiInteractive.this.mc, GuiInteractive.this.width, GuiInteractive.this.height, 32, GuiInteractive.this.height - 65 + 4, 18);
        }

        @Override
        protected int getSize() {
            return GuiInteractive.this.registeredActions.size();
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick, int p_148144_3_, int p_148144_4_) {
            if (doubleClick) {
                GuiInteractive.this.tempAction = GuiInteractive.this.registeredActions.get(index);
            }
        }

        @Override
        protected boolean isSelected(int index) {
            ActionBase action = GuiInteractive.this.getEditAction();
            return action != null && GuiInteractive.this.registeredActions.get(index).getClass().getName().equalsIgnoreCase(action.getClass().getName());
        }

        @Override
        protected void drawBackground() {

        }

        @Override
        protected void drawContainerBackground(Tessellator tessellator) {

        }

        @Override
        protected void drawSlot(int index, int p_148126_2_, int p_148126_3_, int p_148126_4_, Tessellator tessellator, int p_148126_6_, int p_148126_7_) {
            ActionBase action = GuiInteractive.this.registeredActions.get(index);
            GuiInteractive.this.drawCenteredString(GuiInteractive.this.fontRendererObj, TetrisForgeConnector.getActionName(action.getClass().getName()), this.width / 2, p_148126_3_ + 1, 16777215);
        }
    }
}
