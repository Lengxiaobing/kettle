package org.kettle.webapp.trans.steps.checksum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.pentaho.di.trans.steps.checksum.CheckSumMeta;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: 检查总和控制器
 * @author: ZX
 * @date: 2018/11/21 15:16
 */
@Controller
@RequestMapping(value = "/checksum")
public class CheckSumController {

    @RequestMapping(method = RequestMethod.POST, value = "/types")
    @ResponseBody
    protected List types() throws IOException {
        List list = new ArrayList();

        for (String code : CheckSumMeta.checksumtypeCodes) {
            LinkedCaseInsensitiveMap record = new LinkedCaseInsensitiveMap();
            record.put("code", code);
            list.add(record);
        }

        return list;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/resulttype")
    @ResponseBody
    protected List resulttype() throws IOException {
        List list = new ArrayList();

        for (int i = 0; i < CheckSumMeta.resultTypeCode.length; i++) {
            LinkedCaseInsensitiveMap record = new LinkedCaseInsensitiveMap();
            record.put("code", CheckSumMeta.resultTypeCode[i]);
            record.put("desc", CheckSumMeta.resultTypeDesc[i]);
            list.add(record);
        }

        return list;
    }

}
