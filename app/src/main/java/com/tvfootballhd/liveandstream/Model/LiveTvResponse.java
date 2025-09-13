package com.tvfootballhd.liveandstream.Model;

import java.io.Serializable;

public class LiveTvResponse implements Serializable {
    public String cat_id;
    public String category_image;
    public String category_image_thumb;
    public String category_name;
    public String channel_desc;
    public String channel_thumbnail;
    public String channel_title;
    public String channel_url;
    public String channel_url_ios;
    public String cid;
    public String id;
    public String rate_avg;
    public String total_rate;
    public String total_views;

    public String getId() {
        return this.id;
    }

    public String getCat_id() {
        return this.cat_id;
    }

    public String getChannel_title() {
        return this.channel_title;
    }

    public String getChannel_url() {
        return this.channel_url;
    }

    public String getChannel_url_ios() {
        return this.channel_url_ios;
    }

    public String getChannel_thumbnail() {
        return this.channel_thumbnail;
    }

    public String getChannel_desc() {
        return this.channel_desc;
    }

    public String getTotal_views() {
        return this.total_views;
    }

    public String getTotal_rate() {
        return this.total_rate;
    }

    public String getRate_avg() {
        return this.rate_avg;
    }

    public String getCid() {
        return this.cid;
    }

    public String getCategory_name() {
        return this.category_name;
    }

    public String getCategory_image() {
        return this.category_image;
    }

    public String getCategory_image_thumb() {
        return this.category_image_thumb;
    }
}
