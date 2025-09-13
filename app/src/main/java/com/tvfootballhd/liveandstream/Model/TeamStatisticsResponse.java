package com.tvfootballhd.liveandstream.Model;

import java.util.ArrayList;

public class TeamStatisticsResponse {
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

    public class Paging {
        public int current;
        public int total;

        public Paging() {
        }
    }

    public class Parameters {
        public String fixture;

        public Parameters() {
        }
    }

    public class Response {
        public ArrayList<Statistic> statistics;
        public Team team;

        public Response() {
        }

        public Team getTeam() {
            return this.team;
        }

        public ArrayList<Statistic> getStatistics() {
            return this.statistics;
        }
    }

    public class Statistic {
        public String type;
        public Object value;

        public Statistic() {
        }

        public String getType() {
            return this.type;
        }

        public Object getValue() {
            return this.value;
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
}
