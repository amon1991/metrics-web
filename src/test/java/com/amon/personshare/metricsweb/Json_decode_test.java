package com.amon.personshare.metricsweb;

import com.alibaba.fastjson.JSON;
import com.amon.personshare.metricsweb.model.KeyCouple;
import org.junit.Test;

import java.util.List;

/**
 * yaming.chen@siemens.com
 * Created by chenyaming on 2016/7/20.
 */
public class Json_decode_test {

    @Test
    public void testJsonDecode(){

        String jsonStr = "[{appname:\"tsdata\",metricskey:\"com.amon.personshare.metrics_mysql.CounterTest.pedding-jobs_1\"}" +
                ",{appname:\"tsdata\",metricskey:\"com.amon.personshare.metrics_mysql.CounterTest.pedding-jobs_2\"}]";

        List<KeyCouple> keyCouples = JSON.parseArray(jsonStr,KeyCouple.class);

        for (KeyCouple keyCouple : keyCouples) {
            System.out.println(keyCouple);
        }


    }

}
