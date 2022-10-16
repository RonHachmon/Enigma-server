package app.mini_apps.uboat.bodies.absractScene;


import app.mini_apps.uboat.UboatController;
import engine.enigma.machineutils.MachineInformation;
import engine.enigma.machineutils.MachineManager;

public abstract class MainAppScene {
    protected MachineManager machineManager;
    protected UboatController uboatController;
    protected MachineInformation machineInformation;
    public void setMachineManager(MachineManager machineManager) {
        this.machineManager = machineManager;
    }

    public void setMainAppController(UboatController uboatController) {
        this.uboatController = uboatController;
    }
    public void setMachineInformation(MachineInformation machineInformation) {
        this.machineInformation = machineInformation;
    }
}
