package com.itic.intranet.services;

public interface UserClassroomService {
    void assignClassroomToUser(Long userId, Long classroomId);
    void removeClassroomFromUser(Long userId, Long classroomId);
}
