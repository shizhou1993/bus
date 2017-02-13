package com.shizhou.bus.DATA;

import java.util.List;

/**
 * Created by Administrator on 2017/2/6.
 */

public class weather {

    /**
     * code : 1
     * msg : Sucess
     * counts : 29
     * data : {"cityId":"CH010100","cityName":"北京","sj":"2017-02-06 10:00:00","list":[{"tq1":"多云","tq2":"阴","numtq1":"01","numtq2":"02","qw1":"5","qw2":"-5","fl1":"微风","fl2":"微风","numfl1":"0","numfl2":"0","fx1":"南风","fx2":"北风","numfx1":"4","numfx2":"8","date":"2017-02-06"},{"tq1":"阴","numtq1":"02","numtq2":"02","tq2":"阴","qw1":"2","qw2":"-3","fl1":"微风","fl2":"3-4级","numfl1":"0","numfl2":"1","fx1":"南风","fx2":"西北风","numfx1":"4","numfx2":"7","date":"2017-02-07"}]}
     */

    private int code;
    private String msg;
    private int counts;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * cityId : CH010100
         * cityName : 北京
         * sj : 2017-02-06 10:00:00
         * list : [{"tq1":"多云","tq2":"阴","numtq1":"01","numtq2":"02","qw1":"5","qw2":"-5","fl1":"微风","fl2":"微风","numfl1":"0","numfl2":"0","fx1":"南风","fx2":"北风","numfx1":"4","numfx2":"8","date":"2017-02-06"},{"tq1":"阴","numtq1":"02","numtq2":"02","tq2":"阴","qw1":"2","qw2":"-3","fl1":"微风","fl2":"3-4级","numfl1":"0","numfl2":"1","fx1":"南风","fx2":"西北风","numfx1":"4","numfx2":"7","date":"2017-02-07"}]
         */

        private String cityId;
        private String cityName;
        private String sj;
        private List<ListBean> list;

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getSj() {
            return sj;
        }

        public void setSj(String sj) {
            this.sj = sj;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * tq1 : 多云
             * tq2 : 阴
             * numtq1 : 01
             * numtq2 : 02
             * qw1 : 5
             * qw2 : -5
             * fl1 : 微风
             * fl2 : 微风
             * numfl1 : 0
             * numfl2 : 0
             * fx1 : 南风
             * fx2 : 北风
             * numfx1 : 4
             * numfx2 : 8
             * date : 2017-02-06
             */

            private String tq1;
            private String tq2;
            private String numtq1;
            private String numtq2;
            private String qw1;
            private String qw2;
            private String fl1;
            private String fl2;
            private String numfl1;
            private String numfl2;
            private String fx1;
            private String fx2;
            private String numfx1;
            private String numfx2;
            private String date;

            public String getTq1() {
                return tq1;
            }

            public void setTq1(String tq1) {
                this.tq1 = tq1;
            }

            public String getTq2() {
                return tq2;
            }

            public void setTq2(String tq2) {
                this.tq2 = tq2;
            }

            public String getNumtq1() {
                return numtq1;
            }

            public void setNumtq1(String numtq1) {
                this.numtq1 = numtq1;
            }

            public String getNumtq2() {
                return numtq2;
            }

            public void setNumtq2(String numtq2) {
                this.numtq2 = numtq2;
            }

            public String getQw1() {
                return qw1;
            }

            public void setQw1(String qw1) {
                this.qw1 = qw1;
            }

            public String getQw2() {
                return qw2;
            }

            public void setQw2(String qw2) {
                this.qw2 = qw2;
            }

            public String getFl1() {
                return fl1;
            }

            public void setFl1(String fl1) {
                this.fl1 = fl1;
            }

            public String getFl2() {
                return fl2;
            }

            public void setFl2(String fl2) {
                this.fl2 = fl2;
            }

            public String getNumfl1() {
                return numfl1;
            }

            public void setNumfl1(String numfl1) {
                this.numfl1 = numfl1;
            }

            public String getNumfl2() {
                return numfl2;
            }

            public void setNumfl2(String numfl2) {
                this.numfl2 = numfl2;
            }

            public String getFx1() {
                return fx1;
            }

            public void setFx1(String fx1) {
                this.fx1 = fx1;
            }

            public String getFx2() {
                return fx2;
            }

            public void setFx2(String fx2) {
                this.fx2 = fx2;
            }

            public String getNumfx1() {
                return numfx1;
            }

            public void setNumfx1(String numfx1) {
                this.numfx1 = numfx1;
            }

            public String getNumfx2() {
                return numfx2;
            }

            public void setNumfx2(String numfx2) {
                this.numfx2 = numfx2;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }
        }
    }
}
