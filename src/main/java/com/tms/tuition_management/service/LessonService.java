package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Lesson;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface LessonService {
    Lesson saveLesson(Lesson lesson, MultipartFile file) throws IOException;
    List<Lesson> getAllLessons();
    Lesson getLessonById(Long id);
    void deleteLessonById(Long id);
    Resource loadFileAsResource(String fileName) throws MalformedURLException;
}
