package com.ruiao.tools.http.builder;


import com.ruiao.tools.http.OkHttpUtils;
import com.ruiao.tools.http.request.OtherRequest;
import com.ruiao.tools.http.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder {
    @Override
    public RequestCall build() {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers, id).build();
    }
}
