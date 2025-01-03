package hfu.java.todoapp.common.enums;

public enum Priority {
    Priority_1(0),
    Priority_2(1),
    Priority_3(2),
    Priority_4(3),
    Priority_5(4);

    private int value;

    private Priority(int Value) {
        value = Value;
    }

    public int getValue() {
        return value;
    }

    public static Priority getByInt(int value) {
        switch (value) {
            case 0:
                return Priority.Priority_1;
            case 1:
                return Priority.Priority_2;
            case 2:
                return Priority.Priority_3;
            case 3:
                return Priority.Priority_4;
            case 4:
                return Priority.Priority_5;
            default:
                throw new IllegalArgumentException("Value not found");
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case Priority_1:
                return "Highest";
            case Priority_2:
                return "High";
            case Priority_3:
                return "Normal";
            case Priority_4:
                return "Low";
            case Priority_5:
                return "Lowest";
            default:
                throw new IllegalArgumentException("Value not found");
        }
    }
}
