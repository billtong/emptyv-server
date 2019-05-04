package com.empty.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/api/file", produces = "application/json;charset=UTF-8")
public class FileController {

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public @ResponseBody String uploadFile(@RequestParam String[] filepaths, MultipartFile[] files,
			HttpServletResponse res, HttpServletRequest req) {
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String path = req.getSession().getServletContext().getRealPath("/");
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
