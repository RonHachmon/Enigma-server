package app.mini_apps.agent.bodies.absractScene;


import app.mini_apps.agent.AgentController;
import engine.machineutils.MachineInformation;
import engine.machineutils.MachineManager;


public abstract class MainAppScene {
    protected MachineManager machineManager;
    protected AgentController agentController;
    protected MachineInformation machineInformation;
    public void setMachineManager(MachineManager machineManager) {
        this.machineManager = machineManager;
    }

    public void setMainAppController(AgentController agentController) {
        this.agentController = agentController;
    }
    public void setMachineInformation(MachineInformation machineInformation) {
        this.machineInformation = machineInformation;
    }
}
