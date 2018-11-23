package org.kettle.webapp.trans.steps.mergejoin;

import org.pentaho.di.trans.steps.mergejoin.MergeJoinMeta;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 合并加入控制器
 * @author: ZX
 * @date: 2018/11/21 15:24
 */
@Controller
@RequestMapping(value = "/mergejoin")
public class MergeJoinController {

    @RequestMapping(method = RequestMethod.POST, value = "/types")
    @ResponseBody
    protected List types() throws Exception {
        List list = new ArrayList();
        for (int i = 0; i < MergeJoinMeta.join_types.length; i++) {
            LinkedCaseInsensitiveMap record = new LinkedCaseInsensitiveMap();
            record.put("name", MergeJoinMeta.join_types[i]);
            list.add(record);
        }
        return list;
    }
}
