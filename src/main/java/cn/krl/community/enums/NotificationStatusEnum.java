package cn.krl.community.enums;

/**
 * Author:Minamoto
 * Date:2020/12/3,14:31
 */
public enum NotificationStatusEnum {

    UNREAD(0),
    READ(1);
    private int status;

    NotificationStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
