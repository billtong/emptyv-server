package com.empty.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.empty.util.DataTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/file", produces = "application/json;charset=UTF-8")
public class FileController {


    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);


    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String uploadFile(@RequestParam String[] filepaths, MultipartFile[] files,
                             HttpServletResponse res, HttpServletRequest req) {
        LOGGER.info(String.valueOf(files.length));
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String path = req.getSession().getServletContext().getRealPath("/");
                LOGGER.info("hello!!!!");
                LOGGER.info(path);

                path = path.replace("empty-server-1.0.0", "empty-video-files").concat(filepaths[i]);
                File desFile = new File(path);
                if (!desFile.getParentFile().exists()) {
                    desFile.mkdirs();
                }
                try {
                    files[i].transferTo(desFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    res.setStatus(400);
                    return "failed";
                }
            }
            return "success";
        } else {
            res.setStatus(400);
            return "failed";
        }
    }

}
