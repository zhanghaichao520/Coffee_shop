package edu.xjtlu.cpt403.entity;

public enum VipStatusEnum {
    VIP_COSTUMER(1),
    ORDINARY_CUSTOMER(0),
    UNKNOWN_STATUS(-1);
    private int status;

    VipStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static VipStatusEnum getByStatus(int status) {
        for (VipStatusEnum it : VipStatusEnum.values()) {
            if (it.getStatus() == status) {
                return it;
            }
        }
        return UNKNOWN_STATUS;
    }
}
