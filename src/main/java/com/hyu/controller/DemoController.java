package com.hyu.controller;

import com.hyu.entity.DemoEntity;
import com.hyu.util.ExcelReadHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;

@RestController
@RequestMapping("excel")
public class DemoController {

    /**
     * 导入群发信息的用户Id
     * @param file
     * @return
     */
    @RequestMapping(value = "importFromFile")
    public Object importFromFile(@RequestParam("file") CommonsMultipartFile  file) {
        try {
            String[] props = {"a","b","c"};
            List<Object> results =  ExcelReadHelper.excelRead(file.getInputStream(),props,DemoEntity.class);
            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
