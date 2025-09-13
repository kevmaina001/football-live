package com.tvfootballhd.liveandstream.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class SquadResponse implements Serializable {
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
        public String team;

        public Parameters() {
        }
    }

    public class Player implements Serializable {
        public int age;
        public int id;
        public String name;
        public int number;
        public String photo;
        public String position;

        public Player() {
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public int getAge() {
            return this.age;
        }

        public int getNumber() {
            return this.number;
        }

        public String getPosition() {
            return this.position;
        }

        public String getPhoto() {
            return this.photo;
        }
    }

    public class Response implements Serializable {
        public ArrayList<Player> players;
        public Team team;

        public Response() {
        }

        public Team getTeam() {
            return this.team;
        }

        public ArrayList<Player> getPlayers() {
            return this.players;
        }
    }

    public class Team implements Serializable {
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
