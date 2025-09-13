package com.tvfootballhd.liveandstream.Model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class FixtureResponse implements Serializable {
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

    public class Away implements Serializable {
        public int id;
        public String logo;
        public String name;
        public boolean winner;

        public Away() {
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

        public boolean isWinner() {
            return this.winner;
        }
    }

    public class Extratime implements Serializable {
        public int away;
        public int home;

        public Extratime() {
        }
    }

    public class Fixture implements Serializable {
        public Date date;
        public int id;
        public Periods periods;
        public String referee;
        public Status status;
        public int timestamp;
        public String timezone;
        public Venue venue;

        public Fixture() {
        }

        public int getId() {
            return this.id;
        }

        public String getReferee() {
            return this.referee;
        }

        public String getTimezone() {
            return this.timezone;
        }

        public Date getDate() {
            return this.date;
        }

        public int getTimestamp() {
            return this.timestamp;
        }

        public Periods getPeriods() {
            return this.periods;
        }

        public Venue getVenue() {
            return this.venue;
        }

        public Status getStatus() {
            return this.status;
        }
    }

    public class Fulltime implements Serializable {
        public int away;
        public int home;

        public Fulltime() {
        }
    }

    public class Goals implements Serializable {
        public int away;
        public int home;

        public Goals() {
        }

        public int getHome() {
            return this.home;
        }

        public int getAway() {
            return this.away;
        }
    }

    public class Halftime implements Serializable {
        public int away;
        public int home;

        public Halftime() {
        }
    }

    public class Home implements Serializable {
        public int id;
        public String logo;
        public String name;
        public boolean winner;

        public Home() {
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

        public boolean isWinner() {
            return this.winner;
        }
    }

    public class League implements Serializable {
        public String country;
        public String flag;
        public int id;
        public String logo;
        public String name;
        public String round;
        public int season;

        public League() {
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.id;
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

        public String getRound() {
            return this.round;
        }
    }

    public class Paging implements Serializable {
        public int current;
        public int total;

        public Paging() {
        }
    }

    public class Parameters implements Serializable {
        public String date;

        public Parameters() {
        }
    }

    public class Penalty implements Serializable {
        public int away;
        public int home;

        public Penalty() {
        }
    }

    public class Periods implements Serializable {
        public int first;
        public int second;

        public Periods() {
        }
    }

    public class Response implements Serializable {
        public Fixture fixture;
        public Goals goals;
        public League league;
        public Score score;
        public Teams teams;

        public Response() {
        }

        public Goals getGoals() {
            return this.goals;
        }

        public League getLeague() {
            return this.league;
        }

        public Teams getTeams() {
            return this.teams;
        }

        public Fixture getFixture() {
            return this.fixture;
        }
    }

    public class Score implements Serializable {
        public Extratime extratime;
        public Fulltime fulltime;
        public Halftime halftime;
        public Penalty penalty;

        public Score() {
        }
    }

    public class Status implements Serializable {
        public int elapsed;
        public String mylong;
        @SerializedName("short")
        public String myshort;

        public Status() {
        }

        public String getMylong() {
            return this.mylong;
        }

        public String getMyshort() {
            return this.myshort;
        }

        public int getElapsed() {
            return this.elapsed;
        }
    }

    public class Teams implements Serializable {
        public Away away;
        public Home home;

        public Teams() {
        }

        public Home getHome() {
            return this.home;
        }

        public Away getAway() {
            return this.away;
        }
    }

    public class Venue implements Serializable {
        public String city;
        public int id;
        public String name;

        public Venue() {
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public String getCity() {
            return this.city;
        }
    }
}
