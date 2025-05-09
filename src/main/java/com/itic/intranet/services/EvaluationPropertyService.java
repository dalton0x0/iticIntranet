package com.itic.intranet.services;

public interface EvaluationPropertyService {
    void addClassroomToEvaluation(Long evaluationId, Long classroomId);
    void removeClassroomToEvaluation(Long evaluationId, Long classroomId);
}
