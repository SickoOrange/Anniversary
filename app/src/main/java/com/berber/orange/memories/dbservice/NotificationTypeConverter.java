package com.berber.orange.memories.dbservice;

import com.berber.orange.memories.activity.model.NotificationType;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by orange on 2017/11/8.
 */

public class NotificationTypeConverter implements PropertyConverter<NotificationType, String> {
    @Override
    public NotificationType convertToEntityProperty(String databaseValue) {
        return NotificationType.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(NotificationType entityProperty) {
        return entityProperty.name();
    }
}
