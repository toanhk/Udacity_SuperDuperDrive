package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final UserService userService;
    private final EncryptionService encryptionService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final FileService fileService;

    public HomeController(UserService userService, EncryptionService encryptionService, NoteService noteService, CredentialService credentialService, FileService fileService) {
        this.userService = userService;
        this.encryptionService = encryptionService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.fileService = fileService;
    }

    @GetMapping
    public String getHomePage(Model model, Authentication authentication) {
        model.addAttribute("notes", this.noteService.getNotes());
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("credentials", credentialService.getCredentials());
        model.addAttribute("files", fileService.getFiles());
        return "home";
    }

    @PostMapping("/note/update")
    public String updateNote(@ModelAttribute Note note, Model model) {

        int rowsUpdated = noteService.updateNote(note);

        String errorMessage = null;
        if (rowsUpdated < 0) {
            errorMessage = "There was an error. Please try again.";
        }
        if (errorMessage == null) {
            model.addAttribute("changeSuccess", true);
        } else {
            model.addAttribute("changeError", errorMessage);
        }

        return "result";
    }

    @GetMapping("/note/delete/{id}")
    public String deleteNote(@PathVariable("id") int noteId, Model model) {
        int rowsUpdated = noteService.deleteNote(noteId);

        String errorMessage = null;
        if (rowsUpdated < 0) {
            errorMessage = "There was an error. Please try again.";
        }
        if (errorMessage == null) {
            model.addAttribute("changeSuccess", true);
        } else {
            model.addAttribute("changeError", errorMessage);
        }

        return "result";
    }

    @PostMapping("/credential/update")
    public String updateCredential(@ModelAttribute Credential credential, Model model) {

        int rowsUpdated = credentialService.updateCredential(credential);

        String errorMessage = null;
        if (rowsUpdated < 0) {
            errorMessage = "There was an error. Please try again.";
        }
        if (errorMessage == null) {
            model.addAttribute("changeSuccess", true);
        } else {
            model.addAttribute("changeError", errorMessage);
        }

        return "result";
    }

    @GetMapping("/credential/delete/{id}")
    public String deleteCredential(@PathVariable("id") int credentialId, Model model) {
        int rowsUpdated = credentialService.deleteCredential(credentialId);

        String errorMessage = null;
        if (rowsUpdated < 0) {
            errorMessage = "There was an error. Please try again.";
        }
        if (errorMessage == null) {
            model.addAttribute("changeSuccess", true);
        } else {
            model.addAttribute("changeError", errorMessage);
        }

        return "result";
    }

    @PostMapping("/file/upload")
    public String uploadFile(@ModelAttribute MultipartFile fileUpload, Model model) throws IOException, SQLException {
        double size = Double.parseDouble(fileUpload.getSize() + "");
        System.out.println(size);
        int MAX_FILE_SIZE = 4000000;
        try {
            if (size > 0 && size < MAX_FILE_SIZE) {
                int rowsUpdated = fileService.uploadFile(fileUpload);

                String errorMessage = null;
                if (rowsUpdated < 0) {
                    errorMessage = "The file name is existing. Please try another file or name.";
                }
                if (errorMessage == null) {
                    model.addAttribute("changeSuccess", true);
                } else {
                    model.addAttribute("changeError", errorMessage);
                }
            } else {
                model.addAttribute("changeError", "File size exceeds the limit.");
            }
        }
        catch (IOException ex)
        {
            model.addAttribute("changeError", "Error: " + ex.getMessage());

        }

        return "result";
    }

    @GetMapping("/file/view/{id}")
    public HttpEntity<byte[]> viewFile(@PathVariable("id") int fileId, HttpServletResponse response) throws IOException {
        File file = fileService.getFileById(fileId);

        byte[] documentBody = file.getFileData();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf(file.getContentType()));
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + file.getFilename());
        header.setContentLength(documentBody.length);

        return new HttpEntity<byte[]>(documentBody, header);
    }

    @GetMapping("/file/delete/{id}")
    // Method
    public String deleteFile(@PathVariable("id") int fileId, Model model) {
        int rowsUpdated = fileService.deleteFile(fileId);

        String errorMessage = null;
        if (rowsUpdated < 0) {
            errorMessage = "There was an error. Please try again.";
        }
        if (errorMessage == null) {
            model.addAttribute("changeSuccess", true);
        } else {
            model.addAttribute("changeError", errorMessage);
        }

        return "redirect:/home";
    }
}
