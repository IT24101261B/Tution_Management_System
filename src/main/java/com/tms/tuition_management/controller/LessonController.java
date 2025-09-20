package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.Lesson;
import com.tms.tuition_management.repository.LessonRepository;
import com.tms.tuition_management.service.LessonService;
import com.tms.tuition_management.service.TutorService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@Controller
public class LessonController {

    private final LessonService lessonService;
    private final TutorService tutorService;
    private final LessonRepository lessonRepository;

    public LessonController(LessonService lessonService, TutorService tutorService, LessonRepository lessonRepository) {
        this.lessonService = lessonService;
        this.tutorService = tutorService;
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/lessons")
    public String listLessons(Model model) {
        model.addAttribute("lessons", lessonService.getAllLessons());
        return "lessons";
    }

    @GetMapping("/lessons/new")
    public String showUploadForm(Model model) {
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("tutors", tutorService.getAllTutors());
        return "create_lesson";
    }

    @PostMapping("/lessons")
    public String uploadLesson(@ModelAttribute("lesson") Lesson lesson, @RequestParam("file") MultipartFile file, @RequestParam("tutorId") Long tutorId) throws IOException {
        lesson.setTutor(tutorService.getTutorById(tutorId));
        lessonService.saveLesson(lesson, file);
        return "redirect:/lessons";
    }

    @GetMapping("/lessons/edit/{id}")
    public String showEditLessonForm(@PathVariable Long id, Model model) {
        model.addAttribute("lesson", lessonService.getLessonById(id));
        return "edit_lesson";
    }

    @PostMapping("/lessons/update/{id}")
    public String updateLesson(@PathVariable Long id, @ModelAttribute("lesson") Lesson lessonDetails) {
        Lesson existingLesson = lessonService.getLessonById(id);
        if (existingLesson != null) {
            existingLesson.setTitle(lessonDetails.getTitle());
            existingLesson.setDescription(lessonDetails.getDescription());
            lessonRepository.save(existingLesson);
        }
        return "redirect:/lessons";
    }

    @GetMapping("/lessons/delete/{id}")
    public String deleteLesson(@PathVariable Long id) {
        lessonService.deleteLessonById(id);
        return "redirect:/lessons";
    }

    @GetMapping("/lessons/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            Resource resource = lessonService.loadFileAsResource(fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
