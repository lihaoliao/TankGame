package pri.llh.tankgame.enums;

/**
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame.enums
 * @date 2024/01/15 14:46
 * @since 1.0
 */
public enum TankType {
    Player(100),
    EnemyOne(1),
    EnemyTwo(2);

    private final int value;

    TankType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TankType valueOf(int value) {
        for (TankType type : TankType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}

