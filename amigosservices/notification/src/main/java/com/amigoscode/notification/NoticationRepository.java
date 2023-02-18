package com.amigoscode.notification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticationRepository extends JpaRepository<Notification, Integer> {
}
