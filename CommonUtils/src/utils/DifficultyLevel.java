package utils;


public enum DifficultyLevel {

    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    IMPOSSIBLE("Impossible :O");

    private String message;

    private DifficultyLevel(String message) {
        this.message = message;
    }

    public String toString() {
        return this.message;
    }
}
/*    EASY(){
        @Override
        public void setTask(TasksManager tasksManager) throws Exception {
            try {
                tasksManager.setEasyTasks();
            } catch (TaskIsCanceledException e) {
                throw new RuntimeException(e);
            }
        }
    },
    MEDIUM(){
        @Override
        public void setTask(TasksManager tasksManager) throws Exception {
            tasksManager.setMediumTasks();
        }
    },
    HARD(){
        @Override
        public void setTask(TasksManager tasksManager) throws Exception {
            tasksManager.setHardTasks();
        }
    },
    IMPOSSIBLE(){
        @Override
        public void setTask(TasksManager tasksManager) throws Exception {
            tasksManager.setImpossibleTasks();
        }
    };*/


