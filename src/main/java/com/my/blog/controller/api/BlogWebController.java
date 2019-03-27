package com.my.blog.controller.api;

import com.my.common.CommonOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/blog")
public class BlogWebController {

    @Value("${file.blog-image-path}")
    private String imageSavePath;
    @RequestMapping(value = "/getimg", method = RequestMethod.GET)
    public void getImage(@RequestParam(value = "filename") String filename,
                         HttpServletRequest request, HttpServletResponse response)throws IOException {
        CommonOperation.getImage(filename, imageSavePath, request, response);
    }
}
