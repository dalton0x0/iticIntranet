package com.itic.intranet.services;

import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.dtos.UserMinimalDto;
import com.itic.intranet.models.Classroom;

import java.util.List;

public interface ClassroomService {
    List<Classroom> getAllClassrooms();
    Classroom getClassroomById(Long id);
    Classroom createClassroom(ClassroomRequestDto classroomDto);
    Classroom updateClassroom(Long id, ClassroomRequestDto classroomDto);
    void deleteClassroom(Long id);
    Classroom addStudentToClassroom(Long classroomId, Long studentId);
    void removeStudentFromClassroom(Long classroomId, Long studentId);
    Classroom addTeacherToClassroom(Long classroomId, Long teacherId);
    Classroom removeTeacherFromClassroom(Long classroomId, Long teacherId);
    List<UserMinimalDto>  getClassroomStudents(Long classroomId);
    List<UserMinimalDto>  getClassroomTeachers(Long classroomId);
}