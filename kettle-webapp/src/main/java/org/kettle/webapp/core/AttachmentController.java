package org.kettle.webapp.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.kettle.ext.utils.JsonUtils;
import org.kettle.ext.utils.StringEscapeHelper;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: 附件控制器
 * @author: ZX
 * @date: 2018/11/21 14:28
 */
@Controller
@RequestMapping(value = "/attachment")
public class AttachmentController {
    static LinkedList<File> files = new LinkedList<>();

    static {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new ThreadFactoryBuilder().setNameFormat("attachment-pool-%d").build());

        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < files.size(); i++) {
                    if (files.get(i).delete()) {
                        files.remove(i);
                        break;
                    }
                }
            }
        }, 5000, 60 * 1000, TimeUnit.MILLISECONDS);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/download")
    protected void download(@RequestParam String filePath, @RequestParam boolean remove) throws IOException {
        File file = new File(StringEscapeHelper.decode(filePath));
        if (file.isFile()) {
            JsonUtils.download(file);
            if (remove) {
                files.add(file);
            }
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    protected @ResponseBody
    void upload(@RequestParam(value = "file") MultipartFile file) throws IOException {
        File f = new File(file.getOriginalFilename());
        OutputStream os = null;
        InputStream is = null;
        try {
            os = FileUtils.openOutputStream(f);
            is = file.getInputStream();

            FileCopyUtils.copy(is, os);
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }

        JsonUtils.success(f.getAbsolutePath());
    }
}
