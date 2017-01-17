package com.timvisee.safecreeper.task;

import com.timvisee.safecreeper.manager.SCDestructionRepairManager;

public class SCDestructionRepairSaveDataTask extends SCDestructionRepairTask {

    private boolean showMsg = true;

    /**
     * Constructor
     *
     * @param drm     Destruction Repair Manager instance'
     * @param showMsg True to show save statuses in the console
     */
    public SCDestructionRepairSaveDataTask(SCDestructionRepairManager drm, boolean showMsg) {
        super(drm);
        this.showMsg = showMsg;
    }

    /**
     * Task
     */
    @Override
    public void run() {
        // Save the destruction repair data
        getDestructionRepairManager().save(this.showMsg);
    }

    /**
     * Get the task name
     *
     * @return Task name
     */
    public String getTaskName() {
        return "Safe Creeper destruction repair save task";
    }

    /**
     * Check if a message is shown while saving the data
     *
     * @return False if not
     */
    public boolean getShowMessage() {
        return this.showMsg;
    }

    /**
     * Set whether a message should be shown while saving the data
     *
     * @param showMsg True to show a message while saving
     */
    public void setShowMessage(boolean showMsg) {
        this.showMsg = showMsg;
    }
}
