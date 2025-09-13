package com.tvfootballhd.liveandstream.Model;

import java.util.ArrayList;
import java.util.Date;

public class StandingResponse {
    public ArrayList<Object> errors;
    public String f = "for";
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

    public class League {
        public String country;
        public String flag;
        public int id;
        public String logo;
        public String name;
        public int season;
        public ArrayList<ArrayList<Standing>> standings;

        public League() {
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public String getCountry() {
            return this.country;
        }

        public String getLogo() {
            return this.logo;
        }

        public String getFlag() {
            return this.flag;
        }

        public int getSeason() {
            return this.season;
        }

        public ArrayList<ArrayList<Standing>> getStandings() {
            return this.standings;
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
        public String league;
        public String season;

        public Parameters() {
        }

        public String getLeague() {
            return this.league;
        }

        public String getSeason() {
            return this.season;
        }
    }

    public class Response {
        public League league;

        public Response() {
        }

        public League getLeague() {
            return this.league;
        }
    }

    public class All {
        public int draw;
        public Goals goals;
        public int lose;
        public int played;
        public int win;

        public All() {
        }

        public int getPlayed() {
            return this.played;
        }

        public int getWin() {
            return this.win;
        }

        public int getDraw() {
            return this.draw;
        }

        public int getLose() {
            return this.lose;
        }

        public Goals getGoals() {
            return this.goals;
        }
    }

    public class Away {
        public int draw;
        public Goals goals;
        public int lose;
        public int played;
        public int win;

        public Away() {
        }

        public int getPlayed() {
            return this.played;
        }

        public int getWin() {
            return this.win;
        }

        public int getDraw() {
            return this.draw;
        }

        public int getLose() {
            return this.lose;
        }

        public Goals getGoals() {
            return this.goals;
        }
    }

    public class Goals {
        public int against;

        public Goals() {
        }

        public int getAge() {
            return this.against;
        }
    }

    public class Home {
        public int draw;
        public Goals goals;
        public int lose;
        public int played;
        public int win;

        public Home() {
        }

        public int getPlayed() {
            return this.played;
        }

        public int getWin() {
            return this.win;
        }

        public int getDraw() {
            return this.draw;
        }

        public int getLose() {
            return this.lose;
        }

        public Goals getGoals() {
            return this.goals;
        }
    }

    public class Standing {
        public All all;
        public Away away;
        public String description;
        public String form;
        public int goalsDiff;
        public String group;
        public Home home;
        public int points;
        public int rank;
        public String status;
        public Team team;
        public Date update;

        public Standing() {
        }

        public int getRank() {
            return this.rank;
        }

        public Team getTeam() {
            return this.team;
        }

        public int getPoints() {
            return this.points;
        }

        public int getGoalsDiff() {
            return this.goalsDiff;
        }

        public String getGroup() {
            return this.group;
        }

        public String getForm() {
            return this.form;
        }

        public String getStatus() {
            return this.status;
        }

        public String getDescription() {
            return this.description;
        }

        public All getAll() {
            return this.all;
        }

        public Home getHome() {
            return this.home;
        }

        public Away getAway() {
            return this.away;
        }

        public Date getUpdate() {
            return this.update;
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
