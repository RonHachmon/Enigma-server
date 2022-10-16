package app.mini_apps.allies.bodies.absractScene;


import app.mini_apps.allies.AlliesController;
import engine.enigma.machineutils.MachineInformation;
import engine.enigma.machineutils.MachineManager;

public abstract class MainAppScene {
    protected MachineManager machineManager;
    protected AlliesController alliesController;
    protected MachineInformation machineInformation;
    public void setMachineManager(MachineManager machineManager) {
        this.machineManager = machineManager;
    }

    public void setMainAppController(AlliesController alliesController) {
        this.alliesController = alliesController;
    }
    public void setMachineInformation(MachineInformation machineInformation) {
        this.machineInformation = machineInformation;
    }
}
