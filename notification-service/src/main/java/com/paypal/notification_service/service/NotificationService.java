package com.paypal.notification_service.service;

import com.paypal.notification_service.entity.Notification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    Notification sendNotification(Notification notification);

    List<Notification> getNotificationByUserId(Long  userId);
}
