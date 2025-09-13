package com.tvfootballhd.liveandstream.Model;

import java.util.ArrayList;

public class EventResponse {
    public ArrayList<Object> errors;
    public String get;
    public Paging paging;
    public Parameters parameters;
    public ArrayList<Response> response;
    public int results;

    public String getGet() {
        return this.get;
    }

    public Parameters getParameters() {
        return this.parameters;
    }

    public ArrayList<Object> getErrors() {
        return this.errors;
    }

    public int getResults() {
        return this.results;
    }

    public Paging getPaging() {
        return this.paging;
    }

    public ArrayList<Response> getResponse() {
        return this.response;
    }

    public class Assist {
        public int id;
        public String name;

        public Assist() {
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }
    }

    public class Paging {
        public int current;
        public int total;

        public Paging() {
        }

        public int getCurrent() {
            return this.current;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class Parameters {
        public String fixture;

        public Parameters() {
        }

        public String getFixture() {
            return this.fixture;
        }
    }

    public class Player {
        public int id;
        public String name;

        public Player() {
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }
    }

    public class Response {
        public Assist assist;
        public Object comments;
        public String detail;
        public Player player;
        public Team team;
        public Time time;
        public String type;

        public Response() {
        }

        public Time getTime() {
            return this.time;
        }

        public Team getTeam() {
            return this.team;
        }

        public Player getPlayer() {
            return this.player;
        }

        public Assist getAssist() {
            return this.assist;
        }

        public String getType() {
            return this.type;
        }

        public String getDetail() {
            return this.detail;
        }

        public Object getComments() {
            return this.comments;
        }
    }

    public class Team {
        public int id;
        public String logo;
        public String name;

        public Team() {
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public String getLogo() {
            return this.logo;
        }
    }

    public class Time {
        public int elapsed;
        public Object extra;

        public Time() {
        }

        public int getElapsed() {
            return this.elapsed;
        }

        public Object getExtra() {
            return this.extra;
        }
    }
}
